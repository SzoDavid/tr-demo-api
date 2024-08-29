package com.example.trdemoapi.exception;

public class ConflictingStateException extends RuntimeException {
    public ConflictingStateException(String message) {
        super(message);
    }
}
