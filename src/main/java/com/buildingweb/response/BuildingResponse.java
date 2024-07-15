package com.buildingweb.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BuildingResponse {
    private String name;
    private String address;
    private Long numberOfBasement;
    private String managerName;
    private String managerPhonenumber;
    private Long floorArea;
    private Long emptySpace;
    private String leasedArea;
    private Long rentPrice;
    private Long serviceFee;
    private Long brokerageFee;
}