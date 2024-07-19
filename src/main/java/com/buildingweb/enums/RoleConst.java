package com.buildingweb.enums;

import lombok.Getter;

@Getter
public enum RoleConst {
    MANAGER("Quản lý"),
    STAFF("Nhân viên");

    private String name;

    RoleConst(String name) {
        this.name = name;
    }

}