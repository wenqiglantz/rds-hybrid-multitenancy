package com.github.wenqiglantz.service.customer.data;

public enum CustomerStatus {
    CREATED,
    UPDATED,
    DELETED;

    public String value() {
        return name();
    }

    public static CustomerStatus fromValue(String v) {
        return valueOf(v);
    }
}
