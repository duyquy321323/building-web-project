package com.buildingweb.request;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.buildingweb.enums.RoleConst;

import lombok.Getter;

@Getter
public class CreateAccountRequest {
    @NotNull(message = "role is not null")
    private List<RoleConst> roles;
    @NotBlank(message = "username is not blank")
    private String username;
    @NotBlank(message = "fullname is not blank")
    private String fullname;
}