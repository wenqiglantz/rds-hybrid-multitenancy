package com.github.wenqiglantz.service.customer.multitenancy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TenantService {

    private final TenantRepository tenantRepository;

    @Autowired
    public TenantService(TenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }

    public Tenant findByTenantId(String tenantId) {
        return tenantRepository.findByTenantId(tenantId)
                .orElseThrow(() -> new RuntimeException("No such tenant: " + tenantId));
    }

}
