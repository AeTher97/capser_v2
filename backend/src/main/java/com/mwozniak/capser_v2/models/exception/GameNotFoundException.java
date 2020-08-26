package com.mwozniak.capser_v2.models.exception;

import org.springframework.http.HttpStatus;

public class GameNotFoundException extends CapserException {

    public GameNotFoundException(String message){
        super(message);
    }

    public GameNotFoundException(String message,Throwable t){
        super(message, t);
    }

    @Override
    public HttpStatus getStatusCode() {
        return HttpStatus.NOT_FOUND;
    }
}
