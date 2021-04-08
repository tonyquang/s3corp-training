package com.quan12yt.dbmicroservice.exceptions;

public class WrongEmailFormatException extends RuntimeException{

    public WrongEmailFormatException(String message) {
        super(message);
    }

    public WrongEmailFormatException(String message, Throwable t) {
        super(message, t);
    }
}
