package com.buildingweb.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.buildingweb.enums.District;
import com.buildingweb.enums.RentType;

@RestController
@RequestMapping("/util")
public class UtilController {

    @GetMapping("/district-code")
    public Map<String, String> listDistrictCode() {
        return District.listDistrict();
    }

    @GetMapping("/rent-type-code")
    public Map<String, String> listRentTypeCode() {
        return RentType.rentTypes();
    }
}