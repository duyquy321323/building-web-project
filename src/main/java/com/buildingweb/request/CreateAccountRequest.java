package com.buildingweb.request;

import java.util.List;

import javax.validation.constraints.NotBlank;

import com.buildingweb.enums.RoleConst;

import lombok.Getter;

@Getter
public class CreateAccountRequest {
    private List<RoleConst> roles;
    @NotBlank(message = "username is not blank")
    private String username;
    @NotBlank(message = "fullname is not blank")
    private String fullname;
}