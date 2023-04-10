package com.github.wenqiglantz.service.customer.multitenancy.config.tenant.hibernate;

import com.github.wenqiglantz.service.customer.multitenancy.Tenant;
import com.github.wenqiglantz.service.customer.multitenancy.TenantRepository;
import com.github.wenqiglantz.service.customer.util.EncryptionService;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Slf4j
@Component("hybridMultiTenantConnectionProvider")
public class HybridMultiTenantConnectionProvider
        extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {

    private static final String TENANT_POOL_NAME_SUFFIX = "DataSource";

    private final EncryptionService encryptionService;

    @Qualifier("masterDataSource")
    private final DataSource masterDataSource;

    @Qualifier("masterDataSourceProperties")
    private final DataSourceProperties dataSourceProperties;

    private final TenantRepository masterTenantRepository;

    @Value("${multitenancy.tenant.datasource.url-prefix}")
    private String urlPrefix;

    @Value("${multitenancy.datasource-cache.maximumSize:100}")
    private Long maximumSize;

    @Value("${multitenancy.datasource-cache.expireAfterAccess:10}")
    private Integer expireAfterAccess;

    @Value("${encryption.secret}")
    private String secret;

    @Value("${encryption.salt}")
    private String salt;

    private LoadingCache<String, DataSource> tenantDataSources;

    @PostConstruct
    private void createCache() {
        tenantDataSources = CacheBuilder.newBuilder()
                .maximumSize(maximumSize)
                .expireAfterAccess(expireAfterAccess, TimeUnit.MINUTES)
                .removalListener((RemovalListener<String, DataSource>) removal -> {
                    HikariDataSource ds = (HikariDataSource) removal.getValue();
                    ds.close(); // tear down properly
                    log.info("Closed datasource: {}", ds.getPoolName());
                })
                .build(new CacheLoader<String, DataSource>() {
                    public DataSource load(String key) {
                        Tenant tenant = masterTenantRepository.findByTenantId(key)
                                .orElseThrow(() -> new RuntimeException("No such tenant: " + key));
                        return createAndConfigureDataSource(tenant);
                    }
                });
    }

    @Override
    protected DataSource selectAnyDataSource() {
        return masterDataSource;
    }

    @Override
    protected DataSource selectDataSource(String tenantIdentifier) {
        try {
            return tenantDataSources.get(tenantIdentifier);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private DataSource createAndConfigureDataSource(Tenant tenant) {
        String decryptedPassword = encryptionService.decrypt(tenant.getPassword(), secret, salt);
        HikariDataSource ds = dataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
        ds.setUsername(tenant.getUserName());
        ds.setPassword(decryptedPassword);
        ds.setJdbcUrl(tenant.getUrl());
        ds.setPoolName(tenant.getTenantId() + TENANT_POOL_NAME_SUFFIX);

        log.info("Configured datasource: {}", ds.getPoolName());
        log.info("ds url " + ds.getJdbcUrl() + ", user " + ds.getUsername() + ", isolation " + tenant.getIsolationType());
        return ds;
    }

}