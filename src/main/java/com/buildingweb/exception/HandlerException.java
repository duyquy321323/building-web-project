package com.buildingweb.exception;

import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.buildingweb.exception.custom.EntityNotFoundException;
import com.buildingweb.response.ExceptionResponse;

@ControllerAdvice
public class HandlerException {
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> throwNullEntityResponse(DataIntegrityViolationException ex){
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setStatus(HttpStatus.BAD_REQUEST);
        exceptionResponse.setMessage(ex.getMessage());
        List<String> details = new ArrayList<>();
        details.add("please check field data not nullable in entity");
        details.add("this entity is not valid request");
        exceptionResponse.setDetails(details);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> throwEntityNotFound(EntityNotFoundException ex){
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setStatus(HttpStatus.NOT_FOUND);
        exceptionResponse.setMessage(ex.getMessage());
        List<String> details = new ArrayList<>();
        details.add("Data not found in entity. Check please");
        exceptionResponse.setDetails(details);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
    }
}
