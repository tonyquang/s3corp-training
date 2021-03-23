package com.quan12yt.trackingcronjob.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(StartJobFailedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage UserNotFound(StartJobFailedException ex, WebRequest webRequest){
        return new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.value()
                , ex.getMessage()
                , new Date()
                , webRequest.getDescription(false));
    }

    @ExceptionHandler(DataNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage DataNotFound(DataNotFoundException ex, WebRequest webRequest){
        return new ErrorMessage(HttpStatus.BAD_REQUEST.value()
                , ex.getMessage()
                , new Date()
                , webRequest.getDescription(false));
    }

    @ExceptionHandler(VariablesUnacceptedException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage variablesUnacceptedException(VariablesUnacceptedException ex, WebRequest webRequest){
        return new ErrorMessage(HttpStatus.BAD_REQUEST.value()
                , ex.getMessage()
                , new Date()
                , webRequest.getDescription(false));
    }

    @ExceptionHandler(SendEmailFailedException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage sendEmailFailed(SendEmailFailedException ex, WebRequest webRequest){
        return new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.value()
                , ex.getMessage()
                , new Date()
                , webRequest.getDescription(false));
    }

}
