package com.buildingweb.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.buildingweb.request.BuildingRequest;
import com.buildingweb.service.BuildingService;

import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@PropertySource("classpath:application.properties") // lấy biến từ file cấu hình application.properties
public class BuildingController {
    private final BuildingService buildingService;

    @Value("${dev.duyquy321323}") // lấy giá trị của biến có key là dev.duyquy321323
    private String myName;

    @GetMapping("/api/building")
    public ResponseEntity<?> getBuilding(@RequestBody BuildingRequest buildingRequest){
        return ResponseEntity.status(HttpStatus.OK).body(buildingService.searchBuildingByBuildingRequest(buildingRequest));
    }

    @GetMapping("/api/myname")
    public String getMethodName() {
        return myName;
    }
    
}