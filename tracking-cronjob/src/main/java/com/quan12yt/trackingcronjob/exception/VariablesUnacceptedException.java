package com.quan12yt.trackingcronjob.exception;

public class VariablesUnacceptedException extends RuntimeException {

    public VariablesUnacceptedException(String message) {
        super(message);
    }

    public VariablesUnacceptedException(String message, Throwable t) {
        super(message, t);
    }
}
