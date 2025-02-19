package com.nftgunny.core.entities.exceptions;

public class InvalidDataException extends RuntimeException {
    String message;

    public InvalidDataException(String message) {
        super(message);
    }
}
