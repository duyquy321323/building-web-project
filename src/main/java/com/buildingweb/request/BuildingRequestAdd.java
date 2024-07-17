package com.buildingweb.request;

import java.util.List;

import javax.validation.constraints.NotBlank;

import org.springframework.web.multipart.MultipartFile;

import com.buildingweb.enums.District;
import com.buildingweb.enums.RentType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BuildingRequestAdd {
    @NotBlank(message = "name is not blank")
    private String name;
    private String street;
    private String ward;
    private District district;
    private String structure;
    private Long numberOfBasement;
    private Long floorArea;
    private String direction;
    private String level;
    private String rentArea;//
    private Long rentPrice;
    private String rentPriceDescription;
    private Long serviceFee;
    private Long carFee;
    private Long motorFee;
    private Long waterFee;
    private Long overtimeFee;
    private Long electricity;
    private Long deposit;
    private Long payment;
    private Long rentTime;
    private Long decorationTime;
    private String managerName;
    private String managerPhoneNumber;
    private Long brokerageFee;
    private List<RentType> rentTypes;//
    private String note;
    private MultipartFile linkOfBuilding;
}
