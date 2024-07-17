package com.buildingweb.exception.custom;

public class RequestNullException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Your request is null";

    public RequestNullException() {
        super(DEFAULT_MESSAGE);
    }

    public RequestNullException(String mes) {
        super(mes);
    }
}