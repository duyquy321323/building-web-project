package com.buildingweb.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.buildingweb.request.BuildingRequestAdd;
import com.buildingweb.request.BuildingRequestSearch;
import com.buildingweb.service.BuildingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@PropertySource("classpath:application.properties") // lấy biến từ file cấu hình application.properties
@RequestMapping("/buildings")
public class BuildingController {
    private final BuildingService buildingService;

    @GetMapping("/")
    public ResponseEntity<?> getBuilding(@ModelAttribute BuildingRequestSearch buildingRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(buildingService.searchBuildingByBuildingRequest(buildingRequest));
    }

    @GetMapping("/{name}")
    public ResponseEntity<?> getBuildingByNameContaining(@PathVariable("name") String name) {
        return ResponseEntity.status(HttpStatus.OK).body(buildingService.findByNameContaining(name));
    }

    @PostMapping("/")
    public ResponseEntity<?> addBuilding(@Valid @RequestBody BuildingRequestAdd buildingRequest, BindingResult result) { // biến
                                                                                                                         // dùng
                                                                                                                         // để
                                                                                                                         // bắt
                                                                                                                         // lỗi
                                                                                                                         // valid
                                                                                                                         // dùng
                                                                                                                         // ở
                                                                                                                         // sau
                                                                                                                         // tham
                                                                                                                         // số
                                                                                                                         // requestbody
                                                                                                                         // cần
                                                                                                                         // trả
                                                                                                                         // ra
                                                                                                                         // lỗi
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors().stream().map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessages);
        }
        buildingService.addNewBuilding(buildingRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/")
    public ResponseEntity<?> updateBuilding(@RequestParam Long id, @Valid @RequestBody BuildingRequestAdd building,
            BindingResult result) {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors().stream().map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessages);
        }
        buildingService.updateBuilding(id, building);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // @DeleteMapping("/{id}")
    // public ResponseEntity<?> deleteBuilding(@PathVariable("id") Long id){
    // buildingService.deleteBuilding(id);
    // return ResponseEntity.status(HttpStatus.OK).build();
    // }

    @DeleteMapping("/{ids}")
    public ResponseEntity<?> deleteMultiBuilding(@PathVariable("ids") Long[] ids) {
        buildingService.deleteByListId(ids);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}