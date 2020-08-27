package com.mwozniak.capser_v2.models.exception;

import org.springframework.http.HttpStatus;

public class UpdateStatsException extends CapserException{

    public UpdateStatsException(String message){
        super(message);
    }

    public UpdateStatsException(String message,Throwable t){
        super(message, t);
    }

    @Override
    public HttpStatus getStatusCode() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

}
