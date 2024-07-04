package com.buildingweb.request;

import com.buildingweb.model.DistrictDTO;

import lombok.Getter;

@Getter
public class BuildingRequestAdd {
    private String name;
    private DistrictDTO district;
    private String ward;
    private String street;
    private String structure;
    private Integer numberOfBasement;
    private Integer floorArea;
    private String direction;
    private String level;
    private Integer rentPrice;
    private String rentPriceDescription;
    private Integer serviceFee;
    private Integer carFee;
    private Integer overtimeFee;
    private Integer electricity;
    private Integer deposit;
    private Integer payment;
    private Integer rentTime;
    private Integer descorationTime;
    private String managerName;
    private Integer brokerageFee;
    private String note;
    private String managerPhonenumber;
}
