package com.nftgunny.core.entities.exceptions;

public class InvalidArgumentsException extends Exception{
    String message;

    public InvalidArgumentsException(String message) {
        super(message);
    }
}
