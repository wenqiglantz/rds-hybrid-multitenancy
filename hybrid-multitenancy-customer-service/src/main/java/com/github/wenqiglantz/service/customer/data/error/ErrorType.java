package com.github.wenqiglantz.service.customer.data.error;


public enum ErrorType {
    INVALID_REQUEST_DATA,
    UNKNOWN_DATA_ITEM,
    DATA_ALREADY_EXISTS,
    UNEXPECTED_ERROR,
    UPSTREAM_SERVICE_ERROR,
    UNAUTHORIZED_REQUEST,
    FORBIDDEN_REQUEST;

    public static ErrorType fromValue(String v) {
        return valueOf(v);
    }
}

