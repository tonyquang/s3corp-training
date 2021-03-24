package com.quan12yt.trackingcronjob.exception;

public class SendEmailFailedException extends RuntimeException{

    public SendEmailFailedException(String message) {
        super(message);
    }

    public SendEmailFailedException(String message, Throwable t) {
        super(message, t);
    }
}
