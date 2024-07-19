package com.buildingweb.request;

import javax.validation.constraints.NotBlank;

import lombok.Getter;

@Getter
public class EditCustomerRequest {
    @NotBlank(message = "fullname is not blank")
    private String fullname;
    @NotBlank(message = "phone number is not blank")
    private String phone;
    private String email;
    private String nameCompany;
    @NotBlank(message = "demand is not blank")
    private String demand;
    private String status;
}