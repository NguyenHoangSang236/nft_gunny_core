package com.nftgunny.core.entities.exceptions;

public class InsertValidationException extends RuntimeException {
    String message;

    public InsertValidationException(String message) {
        super(message);
    }
}
