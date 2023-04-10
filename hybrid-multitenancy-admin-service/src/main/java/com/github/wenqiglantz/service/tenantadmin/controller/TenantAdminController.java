package com.github.wenqiglantz.service.tenantadmin.controller;

import com.github.wenqiglantz.service.tenantadmin.domain.entity.IsolationType;
import com.github.wenqiglantz.service.tenantadmin.service.TenantAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class TenantAdminController {

    @Autowired
    private TenantAdminService tenantAdminService;

    @PostMapping("/tenants")
    public ResponseEntity<Void> createTenant(@RequestParam String tenantId,
                                             @RequestParam IsolationType isolationType,
                                             @RequestParam String dbOrSchema,
                                             @RequestParam String userName,
                                             @RequestParam String password) {
        tenantAdminService.createTenant(tenantId, isolationType, dbOrSchema, userName, password);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
