package com.github.wenqiglantz.service.tenantadmin.repository;

import com.github.wenqiglantz.service.tenantadmin.domain.entity.Tenant;
import org.springframework.data.repository.CrudRepository;

public interface TenantRepository extends CrudRepository<Tenant, String> {
}