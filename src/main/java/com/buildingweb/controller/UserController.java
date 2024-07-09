package com.buildingweb.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.buildingweb.request.LoginRequest;
import com.buildingweb.request.RegisterRequest;
import com.buildingweb.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/login")
    public ResponseEntity<?> login(@ModelAttribute LoginRequest request) {

        return ResponseEntity.status(HttpStatus.OK).body(userService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        userService.register(request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}