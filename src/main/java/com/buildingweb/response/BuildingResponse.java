package com.buildingweb.response;

import com.buildingweb.entity.Building;
import com.buildingweb.utils.UtilFunction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
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

    public static BuildingResponse fromDomain(Building building){
        return BuildingResponse.builder().name(building.getName()).address(building.getStreet() + "," + building.getWard()+ "," + building.getDistrict().getName()).numberOfBasement(building.getNumberOfBasement()).managerName(building.getManagerName()).managerPhonenumber(building.getManagerPhonenumber()).floorArea(building.getFloorArea()).emptySpace(null).leasedArea(UtilFunction.arrayToString(building.getRentAreas())).rentPrice(building.getRentPrice()).serviceFee(building.getServiceFee()).brokerageFee(building.getBrokerageFee()).build();
    }
}