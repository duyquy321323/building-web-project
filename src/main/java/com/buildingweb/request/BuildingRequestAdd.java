package com.buildingweb.request;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
    @NotBlank(message = "street is not blank")
    private String street;
    @NotBlank(message = "ward is not blank")
    private String ward;
    @NotNull(message = "district is not null")
    private District district;
    private String structure;
    private Long numberOfBasement;
    @NotNull(message = "floor area is not null")
    private Long floorArea;
    private String direction;
    private String level;
    @NotBlank(message = "rent area is not blank")
    private String rentArea;
    @NotNull(message = "rent price is not null")
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
    @NotNull(message = "rent type is not null")
    private List<RentType> rentTypes;
    private String note;
    private MultipartFile linkOfBuilding;
}
