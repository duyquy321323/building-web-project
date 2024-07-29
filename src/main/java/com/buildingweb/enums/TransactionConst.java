package com.buildingweb.enums;

import java.util.Map;
import java.util.TreeMap;

import com.buildingweb.exception.custom.EntityNotFoundException;

import lombok.Getter;

@Getter
public enum TransactionConst {
    CSKH("Chăm sóc khách hàng"),
    DDX("Dẫn đi xem");

    private final String name;

    TransactionConst(String name) {
        this.name = name;
    }

    public static Map<String, String> listTransactionType() {
        Map<String, String> transactionType = new TreeMap<>();
        for (TransactionConst it : TransactionConst.values()) {
            transactionType.put(it.toString(), it.name);
        }
        return transactionType;
    }

    public static TransactionConst fromString(String code) {
        for (TransactionConst it : TransactionConst.values()) {
            if (it.toString().equals(code)) {
                return it;
            }
        }
        throw new EntityNotFoundException("Code transaction not found!");
    }
}