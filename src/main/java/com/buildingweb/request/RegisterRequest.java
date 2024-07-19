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
    @NotBlank(message = "fullname is not blank")
    private String fullname;
    @NotBlank(message = "phone number is not blank")
    private String phoneNumber;
    private String email;
}