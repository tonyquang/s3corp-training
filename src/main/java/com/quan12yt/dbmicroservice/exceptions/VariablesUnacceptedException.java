package com.quan12yt.dbmicroservice.exceptions;

public class VariablesUnacceptedException extends RuntimeException {

    public VariablesUnacceptedException(String message) {
        super(message);
    }

    public VariablesUnacceptedException(String message, Throwable t) {
        super(message, t);
    }
}
