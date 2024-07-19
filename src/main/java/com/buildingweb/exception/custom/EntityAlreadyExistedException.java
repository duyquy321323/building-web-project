package com.buildingweb.exception.custom;

public class EntityAlreadyExistedException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Entity is already existed";

    public EntityAlreadyExistedException() {
        super(DEFAULT_MESSAGE);
    }

    public EntityAlreadyExistedException(String mes) {
        super(mes);
    }
}