package com.buildingweb.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.buildingweb.request.BuildingRequestAdd;
import com.buildingweb.request.BuildingRequestSearch;
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
    public ResponseEntity<?> getBuilding(@RequestBody BuildingRequestSearch buildingRequest){
        return ResponseEntity.status(HttpStatus.OK).body(buildingService.searchBuildingByBuildingRequest(buildingRequest));
    }

    @GetMapping("/api/building/{name}")
    public ResponseEntity<?> getBuildingByNameContaining(@PathVariable("name") String name){
        return ResponseEntity.status(HttpStatus.OK).body(buildingService.findByNameContaining(name));
    }

    @PostMapping("/api/building")
    public ResponseEntity<?> addBuilding(@RequestBody BuildingRequestAdd buildingRequest){
        buildingService.addNewBuilding(buildingRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/api/building")
    public ResponseEntity<?> updateBuilding(@RequestParam Integer id, @RequestBody BuildingRequestAdd building){
        buildingService.updateBuilding(id, building);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // @DeleteMapping("/api/building/{id}")
    // public ResponseEntity<?> deleteBuilding(@PathVariable("id") Integer id){
    //     buildingService.deleteBuilding(id);
    //     return ResponseEntity.status(HttpStatus.OK).build();
    // }

    @DeleteMapping("/api/building/{ids}")
    public ResponseEntity<?> deleteMultiBuilding(@PathVariable("ids") Integer[] ids){
        buildingService.deleteByListId(ids);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/api/myname")
    public String getMethodName() {
        return myName;
    }
    
}