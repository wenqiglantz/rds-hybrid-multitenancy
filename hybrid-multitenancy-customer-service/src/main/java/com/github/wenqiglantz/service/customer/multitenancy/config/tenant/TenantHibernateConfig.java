package com.github.wenqiglantz.service.customer.multitenancy.config.tenant;

import com.github.wenqiglantz.service.customer.multitenancy.Tenant;
import com.github.wenqiglantz.service.customer.multitenancy.TenantRepository;
import com.github.wenqiglantz.service.customer.multitenancy.TenantContext;
import jakarta.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.hibernate5.SpringBeanContainer;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@EnableJpaRepositories(
        basePackages = {"${multitenancy.tenant.repository.packages}" },
        entityManagerFactoryRef = "tenantEntityManagerFactory",
        transactionManagerRef = "tenantTransactionManager"
)
@EnableConfigurationProperties(JpaProperties.class)
public class TenantHibernateConfig {

    private final ConfigurableListableBeanFactory beanFactory;
    private final JpaProperties jpaProperties;

    private final TenantRepository tenantRepository;

    @Value("${multitenancy.tenant.entityManager.packages}")
    private String entityPackages;

    @Autowired
    public TenantHibernateConfig(
            ConfigurableListableBeanFactory beanFactory,
            JpaProperties jpaProperties, TenantRepository tenantRepository) {
        this.beanFactory = beanFactory;
        this.jpaProperties = jpaProperties;
        this.tenantRepository = tenantRepository;
    }

    @Primary
    @Bean("tenantEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean tenantEntityManagerFactory(
            @Qualifier("hybridMultiTenantConnectionProvider")
            MultiTenantConnectionProvider connectionProvider,
            @Qualifier("currentTenantIdentifierResolver") CurrentTenantIdentifierResolver tenantResolver) {
        LocalContainerEntityManagerFactoryBean emfBean = new LocalContainerEntityManagerFactoryBean();
        emfBean.setPersistenceUnitName("tenantdb-persistence-unit");
        emfBean.setPackagesToScan(entityPackages);

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        emfBean.setJpaVendorAdapter(vendorAdapter);

        Map<String, Object> properties = new HashMap<>(this.jpaProperties.getProperties());
        properties.put(AvailableSettings.BEAN_CONTAINER, new SpringBeanContainer(this.beanFactory));

        String tenant = TenantContext.getTenantId();
        if (null == tenant) {
            properties.remove(AvailableSettings.DEFAULT_SCHEMA);
        } else {
            //TODO
            Tenant dbTenant = tenantRepository.findByTenantId(tenant).get();
            switch (dbTenant.getIsolationType()) {
                case DATABASE:
                    break;
                case SCHEMA:
                    properties.remove(AvailableSettings.DEFAULT_SCHEMA);
                    break;
            }
        }

        properties.put(AvailableSettings.MULTI_TENANT_CONNECTION_PROVIDER, connectionProvider);
        properties.put(AvailableSettings.MULTI_TENANT_IDENTIFIER_RESOLVER, tenantResolver);
        emfBean.setJpaPropertyMap(properties);

        log.info("tenantEntityManagerFactory set up successfully!");
        return emfBean;
    }

    @Primary
    @Bean("tenantTransactionManager")
    public JpaTransactionManager tenantTransactionManager(
            @Qualifier("tenantEntityManagerFactory") EntityManagerFactory emf) {
        JpaTransactionManager tenantTransactionManager = new JpaTransactionManager();
        tenantTransactionManager.setEntityManagerFactory(emf);
        return tenantTransactionManager;
    }
}
