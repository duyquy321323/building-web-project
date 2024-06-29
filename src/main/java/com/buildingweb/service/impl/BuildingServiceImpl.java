package com.buildingweb.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.buildingweb.converter.BuildingConverter;
import com.buildingweb.entity.Building;
import com.buildingweb.repository.BuildingRepository;
import com.buildingweb.request.BuildingRequest;
import com.buildingweb.response.BuildingResponse;
import com.buildingweb.service.BuildingService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BuildingServiceImpl implements BuildingService {
    private final BuildingRepository buildingRepository;

    @Autowired
    private BuildingConverter buildingConverter;
    @Override
    public List<BuildingResponse> searchBuildingByBuildingRequest(BuildingRequest buildingRequest) {
        List<Building> buildings = buildingRepository.findBuildingByBuildingRequest(buildingRequest);
        return buildings.stream().map(it -> buildingConverter.toBuildingResponse(it)).collect(Collectors.toList());
    }
}