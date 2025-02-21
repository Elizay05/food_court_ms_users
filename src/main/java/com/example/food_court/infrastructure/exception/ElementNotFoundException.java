package com.example.food_court.infrastructure.exception;

public class ElementNotFoundException extends RuntimeException {
    public ElementNotFoundException(String message) {
        super(message);
    }
}
