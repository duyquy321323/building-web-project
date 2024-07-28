package com.buildingweb.enums;

import java.util.Map;
import java.util.TreeMap;

import lombok.Getter;

@Getter
public enum StatusConst {
    CHUA_XU_LY("Chưa xử lý"),
    DANG_XU_LY("Đang xử lý"),
    DA_XU_LY("Đã xử lý");

    private final String name;

    StatusConst(String name) {
        this.name = name;
    }

    public static Map<String, String> listStatus() {
        Map<String, String> status = new TreeMap<>();
        for (StatusConst it : StatusConst.values()) {
            status.put(it.toString(), it.name);
        }
        return status;
    }
}