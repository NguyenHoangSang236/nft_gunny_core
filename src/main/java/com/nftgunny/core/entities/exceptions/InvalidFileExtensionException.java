package com.nftgunny.core.entities.exceptions;

public class InvalidFileExtensionException extends RuntimeException{
    String message;

    public InvalidFileExtensionException(String message) {
        super(message);
    }
}
