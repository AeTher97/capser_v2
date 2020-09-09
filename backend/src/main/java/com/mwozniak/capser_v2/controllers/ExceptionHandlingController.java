package com.mwozniak.capser_v2.controllers;

import com.mwozniak.capser_v2.models.exception.CapserException;
import com.mwozniak.capser_v2.models.responses.ErrorResponse;
import lombok.extern.log4j.Log4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Log4j
public class ExceptionHandlingController {

    @ExceptionHandler(value = CapserException.class)
    private ResponseEntity<Object> handleException(CapserException e){
        log.error(e.getMessage());

        return ResponseEntity.status(e.getStatusCode().value()).contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorResponse(e.getMessage()));
    }
}
