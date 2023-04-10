package com.github.wenqiglantz.service.customer.multitenancy.config.tenant;

import com.github.wenqiglantz.service.customer.multitenancy.TenantContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component("currentTenantIdentifierResolver")
public class CurrentTenantIdentifierResolverImpl implements CurrentTenantIdentifierResolver,
        HibernatePropertiesCustomizer {

    private final String defaultTenant;

    @Autowired
    public CurrentTenantIdentifierResolverImpl(
            @Value("${multitenancy.master.schema:#{null}}") String defaultTenant) {
        this.defaultTenant = defaultTenant;
    }

    @Override
    public String resolveCurrentTenantIdentifier() {
        String tenantId = TenantContext.getTenantId();
        log.info(">>> tenantId in resolveCurrentTenantIdentifier ", tenantId);
        if (!Strings.isEmpty(tenantId)) {
            return tenantId;
        } else if (!Strings.isEmpty(this.defaultTenant)) {
            return this.defaultTenant;
        } else {
            throw new IllegalStateException("No tenant selected");
        }
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return false;
    }

    @Override
    public void customize(Map<String, Object> hibernateProperties) {
        hibernateProperties.put(AvailableSettings.MULTI_TENANT_IDENTIFIER_RESOLVER, this);
    }
}
