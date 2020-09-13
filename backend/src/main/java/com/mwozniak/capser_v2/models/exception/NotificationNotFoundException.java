package com.mwozniak.capser_v2.models.exception;

import org.springframework.http.HttpStatus;

public class NotificationNotFoundException extends CapserException{

    public NotificationNotFoundException(String message){
        super(message);
    }

    public NotificationNotFoundException(String message,Throwable t){
        super(message,t);
    }

    public  HttpStatus getStatusCode() {
        return HttpStatus.NOT_FOUND;
    }
}
