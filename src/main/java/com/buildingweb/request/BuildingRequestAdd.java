package com.buildingweb.request;

import javax.validation.constraints.NotBlank;

import org.springframework.web.multipart.MultipartFile;

import com.buildingweb.enums.District;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BuildingRequestAdd {
    @NotBlank(message = "name is not blank")
    private String name;
    private District district;
    private String ward;
    private String street;
    private String structure;
    private Long numberOfBasement;
    private Long floorArea;
    private String direction;
    private String level;
    private Long rentPrice;
    private String rentPriceDescription;
    private Long serviceFee;
    private Long carFee;
    private Long overtimeFee;
    private Long electricity;
    private Long deposit;
    private Long payment;
    private Long rentTime;
    private Long descorationTime;
    private String managerName;
    private Long brokerageFee;
    private String note;
    private String managerPhonenumber;
    private MultipartFile linkOfBuilding;
}
