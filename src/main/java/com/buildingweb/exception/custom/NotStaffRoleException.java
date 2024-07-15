package com.buildingweb.exception.custom;

public class NotStaffRoleException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Exist user is not staff in request";

    public NotStaffRoleException() {
        super(DEFAULT_MESSAGE);
    }

    public NotStaffRoleException(String mes) {
        super(mes);
    }
}