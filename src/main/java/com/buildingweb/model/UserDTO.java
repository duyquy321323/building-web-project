package com.buildingweb.model;

import java.util.List;

import com.buildingweb.enums.RoleConst;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private Long id;
    private String fullname;
    private String username;
    private List<RoleConst> roles;
}