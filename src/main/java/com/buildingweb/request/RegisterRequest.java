package com.buildingweb.request;

import lombok.Getter;

@Getter
public class RegisterRequest {
    private String username;
    private String password;
    private String fullname;
    private String phoneNumber;
    private String email;
    private Integer status;
}