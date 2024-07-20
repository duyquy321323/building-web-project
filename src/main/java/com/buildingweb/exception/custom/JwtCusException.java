package com.buildingweb.exception.custom;

public class JwtCusException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Token error";

    public JwtCusException() {
        super(DEFAULT_MESSAGE);
    }

    public JwtCusException(String mes) {
        super(mes);
    }
}