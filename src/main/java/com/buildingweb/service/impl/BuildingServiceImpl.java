package com.buildingweb.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.buildingweb.converter.BuildingConverter;
import com.buildingweb.entity.Building;
import com.buildingweb.exception.custom.EntityNotFoundException;
import com.buildingweb.repository.BuildingRepository;
import com.buildingweb.request.BuildingRequestAdd;
import com.buildingweb.request.BuildingRequestSearch;
import com.buildingweb.response.BuildingResponse;
import com.buildingweb.service.BuildingService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class BuildingServiceImpl implements BuildingService {
    private final BuildingRepository buildingRepository;

    @Autowired
    private BuildingConverter buildingConverter;
    @Override
    public List<BuildingResponse> searchBuildingByBuildingRequest(BuildingRequestSearch buildingRequest) {
        List<Building> buildings = buildingRepository.findByBuildingRequestSearch(buildingRequest);
        return buildings.stream().map((it) -> buildingConverter.toBuildingResponse(it)).collect(Collectors.toList());
    }

    @Override
    public void addNewBuilding(BuildingRequestAdd buildingRequest) {
        Building building = buildingConverter.buildingRequestAddToBuilding(buildingRequest);
        buildingRepository.save(building);
    }

    @Override
    public void updateBuilding(Long id, BuildingRequestAdd building) {
        if(buildingRepository.existsById(id)){
            Building newBuilding = buildingConverter.buildingRequestAddToBuildingExisted(id, building);
            buildingRepository.save(newBuilding);
        }
        else throw new EntityNotFoundException("Building is not found");
    }

    // @Override
    // public void deleteBuilding(Long id) {
    //     Building building = buildingRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Building is not found"));
    //     if(building != null){
    //         buildingRepository.delete(building);
    //     }
    // }

    @Override
    public void deleteByListId(Long[] ids) {
        buildingRepository.deleteByIdIn(ids);
    }

    @Override
    public List<BuildingResponse> findByNameContaining(String s) {
        List<Building> buildings = buildingRepository.findByNameContaining(s);
        return buildings.stream().map((it) -> buildingConverter.toBuildingResponse(it)).collect(Collectors.toList());
    }
}