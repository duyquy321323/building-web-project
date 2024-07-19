package com.buildingweb.exception.custom;

public class PasswordNotMatchException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "password is not match";

    public PasswordNotMatchException() {
        super(DEFAULT_MESSAGE);
    }

    public PasswordNotMatchException(String mes) {
        super(mes);
    }
}