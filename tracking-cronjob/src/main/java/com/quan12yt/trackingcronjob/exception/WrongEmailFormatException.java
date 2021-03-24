package com.quan12yt.trackingcronjob.exception;

public class WrongEmailFormatException extends RuntimeException{

    public WrongEmailFormatException(String message) {
        super(message);
    }

    public WrongEmailFormatException(String message, Throwable t) {
        super(message, t);
    }
}
