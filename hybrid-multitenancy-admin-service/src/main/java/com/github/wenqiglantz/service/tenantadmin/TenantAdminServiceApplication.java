package com.github.wenqiglantz.service.tenantadmin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class, LiquibaseAutoConfiguration.class })
public class TenantAdminServiceApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(TenantAdminServiceApplication.class, args);
    }
}

