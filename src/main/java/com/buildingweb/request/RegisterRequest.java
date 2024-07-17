package com.buildingweb.request;

import javax.validation.constraints.NotBlank;

import lombok.Getter;

@Getter
public class RegisterRequest {
    @NotBlank(message = "username is not blank")
    private String username;
    @NotBlank(message = "password id not blank")
    private String password;
    private String confirmPassword;
    private String fullname;
    private String phoneNumber;
    private String email;
}