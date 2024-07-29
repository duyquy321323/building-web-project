package com.buildingweb.exception;

import java.security.InvalidParameterException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.buildingweb.exception.custom.EntityAlreadyExistedException;
import com.buildingweb.exception.custom.EntityNotFoundException;
import com.buildingweb.exception.custom.NotAllowRoleException;
import com.buildingweb.exception.custom.PasswordNotMatchException;
import com.buildingweb.exception.custom.RequestNullException;
import com.buildingweb.response.ExceptionResponse;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import io.jsonwebtoken.ExpiredJwtException;

@ControllerAdvice
public class HandlerException {
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> throwNullEntityResponse(DataIntegrityViolationException ex) {
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setStatus(HttpStatus.BAD_REQUEST);
        exceptionResponse.setMessage(ex.getMessage());
        List<String> details = new ArrayList<>();
        details.add("this entity is not valid request");
        exceptionResponse.setDetails(details);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> throwEntityNotFound(EntityNotFoundException ex) {
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setStatus(HttpStatus.NOT_FOUND);
        exceptionResponse.setMessage(ex.getMessage());
        List<String> details = new ArrayList<>();
        details.add("Data not found in entity. Check please");
        exceptionResponse.setDetails(details);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionResponse);
    }

    @ExceptionHandler(PasswordNotMatchException.class)
    public ResponseEntity<?> throwPasswordNotMatch(PasswordNotMatchException ex) {
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setStatus(HttpStatus.BAD_REQUEST);
        exceptionResponse.setMessage(ex.getMessage());
        List<String> details = new ArrayList<>();
        details.add("Please check your password");
        exceptionResponse.setDetails(details);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
    }

    @ExceptionHandler(NotAllowRoleException.class)
    public ResponseEntity<?> throwNotAllowRole(NotAllowRoleException ex) {
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setStatus(HttpStatus.UNAUTHORIZED);
        exceptionResponse.setMessage(ex.getMessage());
        List<String> details = new ArrayList<>();
        details.add("Your role is not allow");
        exceptionResponse.setDetails(details);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exceptionResponse);
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<?> throwNumberFormat(NumberFormatException ex) {
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setStatus(HttpStatus.BAD_REQUEST);
        exceptionResponse.setMessage(ex.getMessage());
        List<String> details = new ArrayList<>();
        details.add("Please check your request is not number");
        exceptionResponse.setDetails(details);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
    }

    @ExceptionHandler(RequestNullException.class)
    public ResponseEntity<?> throwRequestNull(RequestNullException ex) {
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setStatus(HttpStatus.BAD_REQUEST);
        exceptionResponse.setMessage(ex.getMessage());
        List<String> details = new ArrayList<>();
        details.add("Please check your request is null");
        exceptionResponse.setDetails(details);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
    }

    @ExceptionHandler(EntityAlreadyExistedException.class)
    public ResponseEntity<?> throwEntityAlreadyExisted(EntityAlreadyExistedException ex) {
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setStatus(HttpStatus.BAD_REQUEST);
        exceptionResponse.setMessage(ex.getMessage());
        List<String> details = new ArrayList<>();
        details.add("Please check field in your request");
        exceptionResponse.setDetails(details);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
    }

    @ExceptionHandler(InvalidParameterException.class)
    public ResponseEntity<?> throwInvalidCreateToken(InvalidParameterException ex) {
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setStatus(HttpStatus.BAD_REQUEST);
        exceptionResponse.setMessage(ex.getMessage());
        List<String> details = new ArrayList<>();
        details.add("Token can't create");
        exceptionResponse.setDetails(details);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<?> throwInvalidFormat(InvalidFormatException ex) {
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setStatus(HttpStatus.BAD_REQUEST);
        exceptionResponse.setMessage(ex.getMessage());
        List<String> details = new ArrayList<>();
        details.add("Check your request format");
        exceptionResponse.setDetails(details);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<?> throwJwtException(ExpiredJwtException ex) {
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setMessage(ex.getMessage());
        exceptionResponse.setStatus(HttpStatus.BAD_REQUEST);
        List<String> details = new ArrayList<>();
        details.add("Please check token");
        exceptionResponse.setDetails(details);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<?> throwSQLException(SQLException ex) {
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setMessage(ex.getMessage());
        exceptionResponse.setStatus(HttpStatus.BAD_REQUEST);
        List<String> details = new ArrayList<>();
        details.add("Please check request");
        exceptionResponse.setDetails(details);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
    }

}
