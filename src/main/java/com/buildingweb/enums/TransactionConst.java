package com.buildingweb.enums;

import lombok.Getter;

@Getter
public enum TransactionConst {
    CSKH("Chăm sóc khách hàng"),
    DDX("Dẫn đi xem");

    private String name;

    TransactionConst(String name) {
        this.name = name;
    }
}