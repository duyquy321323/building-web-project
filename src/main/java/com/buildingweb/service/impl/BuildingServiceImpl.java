package com.buildingweb.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import lombok.SneakyThrows;

@Service
@RequiredArgsConstructor
@Transactional
public class BuildingServiceImpl implements BuildingService {
    private final BuildingRepository buildingRepository;

    @Autowired
    private BuildingConverter buildingConverter;

    @Override
    public Page<BuildingResponse> searchBuildingByBuildingRequest(BuildingRequestSearch buildingRequest,
            Pageable pageable) {
        Page<Building> buildings = buildingRepository.findByBuildingRequestSearch(buildingRequest, pageable);
        return buildings.map((it) -> buildingConverter.toBuildingResponse(it));
    }

    @Override
    @SneakyThrows
    public void addNewBuilding(BuildingRequestAdd buildingRequest) {
        Building building = buildingConverter.buildingRequestAddToBuilding(buildingRequest);
        if (buildingRequest.getLinkOfBuilding() != null) {
            building.setLinkOfBuilding(buildingRequest.getLinkOfBuilding().getBytes());
        }
        buildingRepository.save(building);
    }

    @Override
    @SneakyThrows
    public void updateBuilding(Long id, BuildingRequestAdd building) {
        if (buildingRepository.existsById(id)) {
            Building newBuilding = buildingConverter.buildingRequestAddToBuildingExisted(id, building);
            if (building.getLinkOfBuilding() != null) {
                newBuilding.setLinkOfBuilding(building.getLinkOfBuilding().getBytes());
            }
            buildingRepository.save(newBuilding);
        } else
            throw new EntityNotFoundException("Building is not found");
    }

    @Override
    public void deleteByListId(Long[] ids) {
        buildingRepository.deleteByIdIn(ids);
    }

    @Override
    public Page<BuildingResponse> findByNameContaining(String s, Pageable pageable) {
        Page<Building> buildings = buildingRepository.findByNameContaining(s, pageable);
        return buildings.map((it) -> buildingConverter.toBuildingResponse(it));
    }
}