package com.buildingweb.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerSearchResponse {
    private Long id;
    private String fullname;
    private String phone;
    private String email;
    private String demand;
    private String createdBy;
    private String createdDate;
    private String status;
}