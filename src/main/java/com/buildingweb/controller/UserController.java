package com.buildingweb.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.buildingweb.request.LoginRequest;
import com.buildingweb.request.RegisterRequest;
import com.buildingweb.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Tag(name = "User Controller", description = "Used for user operations and management.")
@RequestMapping("/account")
public class UserController {

    private final UserService userService;

    // Login
    @Operation(summary = "Login", description = "Sign in your account.")
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request,
            BindingResult result, HttpServletRequest request2, HttpServletResponse response) {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors().stream().map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessages);
        }
        return ResponseEntity.status(HttpStatus.OK).body(userService.login(request, request2, response));
    }

    // Register
    @Operation(summary = "Register", description = "Sign up your new account.")
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errorMessage = result.getFieldErrors().stream().map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }
        userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Logout", description = "Logout this account")
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        return null;
    }
}