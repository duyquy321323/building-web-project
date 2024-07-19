package com.buildingweb.request;

import lombok.Getter;

@Getter
public class CustomerSearchRequest {
    private String name;
    private String phone;
    private String email;
    private Long staffId;
}