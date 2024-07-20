package com.buildingweb.enums;

import lombok.Getter;

@Getter
public enum StatusConst {
    CHUA_XU_LY("Chưa xử lý"),
    DANG_XU_LY("Đang xử lý"),
    DA_XU_LY("Đã xử lý");

    private String name;

    StatusConst(String name) {
        this.name = name;
    }
}