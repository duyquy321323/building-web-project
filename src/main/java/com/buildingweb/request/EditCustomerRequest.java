package com.buildingweb.request;

import lombok.Getter;

@Getter
public class EditCustomerRequest {
    private String fullname;
    private String phone;
    private String email;
    private String nameCompany;
    private String demand;
    private String status;
}