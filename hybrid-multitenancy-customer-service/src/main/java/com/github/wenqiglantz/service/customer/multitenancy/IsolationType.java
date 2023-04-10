package com.github.wenqiglantz.service.customer.multitenancy;

public enum IsolationType {
    DATABASE,
    SCHEMA,
    DISCRIMINATOR;

    public String value() {
        return name();
    }

    public static IsolationType fromValue(String v) {
        return valueOf(v);
    }
}
