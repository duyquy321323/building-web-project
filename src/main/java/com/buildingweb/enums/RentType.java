package com.buildingweb.enums;

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
}