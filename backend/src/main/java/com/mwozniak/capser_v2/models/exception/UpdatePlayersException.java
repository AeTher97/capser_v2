package com.mwozniak.capser_v2.models.exception;

import org.springframework.http.HttpStatus;

public class UpdatePlayersException  extends CapserException{

    public UpdatePlayersException(){
        super("Player is already in the tournament");
    }
    public UpdatePlayersException(String message){
        super(message);
    }

    public UpdatePlayersException(String message,Throwable t){
        super(message, t);
    }

    @Override
    public HttpStatus getStatusCode() {
        return HttpStatus.BAD_REQUEST;
    }
}
