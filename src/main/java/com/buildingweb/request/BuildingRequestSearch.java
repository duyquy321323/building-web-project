package com.buildingweb.request;

import java.util.List;

import com.buildingweb.model.DistrictDTO;
import com.buildingweb.model.RentTypeDTO;
import com.buildingweb.model.UserDTO;

import lombok.Getter;

@Getter
public class BuildingRequestSearch {
    private String name;
    private Integer floorArea;
    private DistrictDTO district;
    private String ward;
    private String street;
    private Integer numberOfBasement;
    private String direction;
    private String level;
    private Integer areaFrom;
    private Integer areaTo;
    private Integer rentPriceFrom;
    private Integer rentPriceTo;
    private String managerName;
    private String managerPhonenumber;
    private UserDTO user;
    private List<RentTypeDTO> rentTypes;
}