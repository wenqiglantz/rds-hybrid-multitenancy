package com.github.wenqiglantz.service.tenantadmin.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Tenant {

    @Id
    @Column(name = "tenant_id")
    private String tenantId;

    @Column(name = "isolation_type")
    @Enumerated(EnumType.STRING)
    private IsolationType isolationType;

    @Column(name = "db_or_schema")
    private String dbOrSchema;

    @Column(name = "url")
    private String url;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

}