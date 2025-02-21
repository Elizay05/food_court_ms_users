package com.example.food_court.infrastructure.exception;

public class FieldAlreadyExistsException extends RuntimeException {

    public FieldAlreadyExistsException(String message) {
        super(message);
    }
}
