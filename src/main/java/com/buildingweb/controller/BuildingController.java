package com.buildingweb.controller;

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
public class BuildingController {
    private final BuildingService buildingService;

    @GetMapping("/api/building")
    public ResponseEntity<?> getBuilding(@RequestBody BuildingRequest buildingRequest){
        return ResponseEntity.status(HttpStatus.OK).body(buildingService.searchBuildingByBuildingRequest(buildingRequest));
    }
}