package com.mwozniak.capser_v2.models.exception;

import org.springframework.http.HttpStatus;

public class TournamentNotFoundException extends CapserException{

    public TournamentNotFoundException(){
        super("Tournament not found");
    }
    public TournamentNotFoundException(String message){
        super(message);
    }

    public TournamentNotFoundException(String message,Throwable t){
        super(message, t);
    }

    @Override
    public HttpStatus getStatusCode() {
        return HttpStatus.NOT_FOUND;
    }
}

