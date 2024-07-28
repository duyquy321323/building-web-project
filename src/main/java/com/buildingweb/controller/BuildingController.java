package com.buildingweb.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@PropertySource("classpath:application.properties") // lấy biến từ file cấu hình application.properties
@RequestMapping("/buildings")
@Tag(name = "Building Controller", description = "Used for building operations and management.")
public class BuildingController {
    private final BuildingService buildingService;

    // Tìm kiếm tòa nhà
    @Operation(summary = "Search building", description = "Search building in database now.")
    @PostMapping("/search")
    public ResponseEntity<?> getBuilding(@RequestBody BuildingRequestSearch buildingRequest,
            @RequestParam(required = false, defaultValue = "0") Integer pageNo,
            @RequestParam(required = false, defaultValue = "2") Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return ResponseEntity.status(HttpStatus.OK)
                .body(buildingService.searchBuildingByBuildingRequest(buildingRequest, pageable));
    }

    // Lấy tòa nhà theo tên
    @GetMapping("/{name}")
    @Operation(summary = "Get building by name", description = "Get building has name contains your enter name.")
    public ResponseEntity<?> getBuildingByNameContaining(@PathVariable("name") String name,
            @RequestParam(required = false, defaultValue = "0") Integer pageNo,
            @RequestParam(required = false, defaultValue = "2") Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return ResponseEntity.status(HttpStatus.OK).body(buildingService.findByNameContaining(name, pageable));
    }

    @PostMapping("/new")
    @Operation(summary = "Add new building", description = "Add new building to database multipart/form-data.", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "multipart/form-data", schema = @Schema(implementation = BuildingRequestAdd.class))))
    public ResponseEntity<?> addBuilding(@Valid BuildingRequestAdd buildingRequest, BindingResult result) { // biến
                                                                                                            // dùng
                                                                                                            // để
                                                                                                            // bắt
                                                                                                            // lỗi
        // valid dùng ở sau tham số
        // requestbody cần trả ra
        // lỗi
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors().stream().map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessages);
        }
        buildingService.addNewBuilding(buildingRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // Sửa tòa nhà
    @PutMapping("/")
    @Operation(summary = "Edit building", description = "Edit building to database multipart/form-data.", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "multipart/form-data", schema = @Schema(implementation = BuildingRequestAdd.class))))
    public ResponseEntity<?> updateBuilding(@RequestParam Long id, BuildingRequestAdd building) {
        buildingService.updateBuilding(id, building);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // Xóa tòa nhà theo danh sách id
    @Operation(summary = "Delete building", description = "Delete building by id.")
    @DeleteMapping("/{ids}")
    public ResponseEntity<?> deleteMultiBuilding(@PathVariable("ids") Long[] ids) {
        buildingService.deleteByListId(ids);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}