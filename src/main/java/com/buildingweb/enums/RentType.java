package com.buildingweb.enums;

import java.util.Map;
import java.util.TreeMap;

import lombok.Getter;

@Getter
public enum RentType {
    TANG_TRET("Tầng trệt"),
    NGUYEN_CAN("Nguyên căn"),
    NOI_THAT("Nội thất");

    private final String name;

    RentType(String name) {
        this.name = name;
    }

    public static Map<String, String> listRentType() {
        Map<String, String> rentTypes = new TreeMap<>();
        for (RentType it : RentType.values()) {
            rentTypes.put(it.toString(), it.name);
        }
        return rentTypes;
    }
}