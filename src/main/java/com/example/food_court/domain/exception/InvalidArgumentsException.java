package com.example.food_court.domain.exception;

public class InvalidArgumentsException extends RuntimeException {
    public InvalidArgumentsException( String message ) {
        super( message );
    }
}
