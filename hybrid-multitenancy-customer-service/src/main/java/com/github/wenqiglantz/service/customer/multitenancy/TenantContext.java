package com.github.wenqiglantz.service.customer.multitenancy;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class TenantContext {

    private TenantContext() {}

    private static final InheritableThreadLocal<String> CURRENT_TENANT = new InheritableThreadLocal<>();

    public static void setTenantId(String tenantId) {
        log.debug("Setting tenantId to " + tenantId);
        CURRENT_TENANT.set(tenantId);
    }

    public static String getTenantId() {
        return CURRENT_TENANT.get();
    }

    public static void clear(){
        CURRENT_TENANT.remove();
    }
}
