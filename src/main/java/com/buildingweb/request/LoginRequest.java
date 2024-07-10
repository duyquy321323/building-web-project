package com.buildingweb.request;

import javax.validation.constraints.NotBlank;

import lombok.Getter;

@Getter
public class LoginRequest {
    @NotBlank(message = "username is not blank")
    private String username;
    @NotBlank(message = "password is not blank")
    private String password;
}