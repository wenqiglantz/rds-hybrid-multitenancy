package com.github.wenqiglantz.service.customer.multitenancy.config.tenant;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import com.github.wenqiglantz.service.customer.multitenancy.config.tenant.liquibase.HybridMultiTenantSpringLiquibase;

@Slf4j
@Lazy(false)
@Configuration
@ConditionalOnProperty(name = "multitenancy.tenant.liquibase.enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(LiquibaseProperties.class)
public class TenantLiquibaseConfig {

    @Value("${multitenancy.tenant.liquibase.changeLog}")
    private String tenantLiquibaseChangeLog;

    @Bean
    @DependsOn("liquibase")
    public HybridMultiTenantSpringLiquibase hybridcMultiTenantSpringLiquibase(
            @Qualifier("masterLiquibaseProperties")
            LiquibaseProperties liquibaseProperties) {
        HybridMultiTenantSpringLiquibase liquibase = new HybridMultiTenantSpringLiquibase();
        liquibase.setChangeLog(tenantLiquibaseChangeLog);
        liquibase.setDropFirst(liquibaseProperties.isDropFirst());
        liquibase.setShouldRun(liquibaseProperties.isEnabled());
        return liquibase;
    }

    @Bean
    @ConfigurationProperties("multitenancy.tenant.liquibase")
    public LiquibaseProperties tenantLiquibaseProperties() {
        return new LiquibaseProperties();
    }

    @Bean
    public HybridMultiTenantSpringLiquibase tenantLiquibase() {
        return new HybridMultiTenantSpringLiquibase();
    }
}