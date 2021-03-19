package com.quan12yt.trackingcronjob.exception;

public class StartJobFailedException extends RuntimeException {

    public StartJobFailedException(String message) {
        super(message);
    }

    public StartJobFailedException(String message, Throwable t) {
        super(message, t);
    }
}
