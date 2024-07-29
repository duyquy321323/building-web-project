package com.buildingweb.enums;

import java.util.Map;
import java.util.TreeMap;

import lombok.Getter;

@Getter
public enum RoleConst {
    MANAGER("Quản lý"),
    STAFF("Nhân viên");

    private final String name;

    RoleConst(String name) {
        this.name = name;
    }

    public static Map<String, String> listRole() {
        Map<String, String> roles = new TreeMap<>();
        for (RoleConst it : RoleConst.values()) {
            roles.put(it.toString(), it.name);
        }
        return roles;
    }
}