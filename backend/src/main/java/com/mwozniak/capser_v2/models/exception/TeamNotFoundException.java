package com.mwozniak.capser_v2.models.exception;

import org.springframework.http.HttpStatus;

public class TeamNotFoundException  extends CapserException{

    public TeamNotFoundException(String message){
        super(message);
    }

    public TeamNotFoundException(String message,Throwable t){
        super(message, t);
    }

    @Override
    public HttpStatus getStatusCode() {
        return HttpStatus.NOT_FOUND;
    }
}
