package com.buildingweb.request;

import java.util.List;

import com.buildingweb.enums.District;
import com.buildingweb.enums.RentType;
import com.buildingweb.model.UserDTO;

import lombok.Getter;

@Getter
public class BuildingRequestSearch {
    private String name;
    private Long floorArea;
    private District district;
    private String ward;
    private String street;
    private Long numberOfBasement;
    private String direction;
    private String level;
    private Long areaFrom;
    private Long areaTo;
    private Long rentPriceFrom;
    private Long rentPriceTo;
    private String managerName;
    private String managerPhoneNumber;
    private UserDTO user;
    private List<RentType> rentTypes;
}