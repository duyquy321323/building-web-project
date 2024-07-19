package com.buildingweb.response;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerSearchResponse {
    private String fullname;
    private String phone;
    private String email;
    private String demand;
    private String createdBy;
    private Date createdDate;
}