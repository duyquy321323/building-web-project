package com.buildingweb.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BuildingResponse {
    private String name;
    private String address;
    private Long numberOfBasement;
    private String managerName;
    private String managerPhoneNumber;
    private Long floorArea;
    private Long emptySpace;
    private String leasedArea;
    private Long rentPrice;
    private Long serviceFee;
    private Long brokerageFee;
}