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
    private Integer numberOfBasement;
    private String managerName;
    private String managerPhonenumber;
    private Integer floorArea;
    private Integer emptySpace;
    private String leasedArea;
    private Integer rentPrice;
    private Integer serviceFee;
    private Integer brokerageFee;
}