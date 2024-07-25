package com.buildingweb.model;

import java.util.List;

import com.buildingweb.enums.RoleConst;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String fullname;
    private String token;
    private Long expiryTime;
    private List<RoleConst> roles;
}