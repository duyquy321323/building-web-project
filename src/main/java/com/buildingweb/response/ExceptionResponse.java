package com.buildingweb.response;

import java.util.List;

import org.springframework.http.HttpStatus;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ExceptionResponse {
    private HttpStatus status;
    private String message;
    private List<String> details;
}