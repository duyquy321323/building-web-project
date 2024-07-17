package com.buildingweb.exception.custom;

public class NotAllowRoleException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Exist user is not staff in request";

    public NotAllowRoleException() {
        super(DEFAULT_MESSAGE);
    }

    public NotAllowRoleException(String mes) {
        super(mes);
    }
}