package com.quan12yt.dbmicroservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(DataNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage dataNotFound(DataNotFoundException ex, WebRequest webRequest){
        return new ErrorMessage(HttpStatus.NOT_FOUND.value()
                , ex.getMessage()
                , new Date()
                , webRequest.getDescription(false));
    }

    @ExceptionHandler(VariablesUnacceptedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage variablesUnacceptedException(VariablesUnacceptedException ex, WebRequest webRequest){
        return new ErrorMessage(HttpStatus.BAD_REQUEST.value()
                , ex.getMessage()
                , new Date()
                , webRequest.getDescription(false));
    }

    @ExceptionHandler(SendEmailFailedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage sendEmailFailed(SendEmailFailedException ex, WebRequest webRequest){
        return new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.value()
                , ex.getMessage()
                , new Date()
                , webRequest.getDescription(false));
    }


}
