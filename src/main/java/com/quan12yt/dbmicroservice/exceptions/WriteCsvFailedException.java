package com.quan12yt.dbmicroservice.exceptions;

public class WriteCsvFailedException extends RuntimeException{

    public WriteCsvFailedException(String message) {
        super(message);
    }

    public WriteCsvFailedException(String message, Throwable t) {
        super(message, t);
    }
}
