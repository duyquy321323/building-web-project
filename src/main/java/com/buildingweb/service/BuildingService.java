package com.buildingweb.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.buildingweb.request.BuildingRequestAdd;
import com.buildingweb.request.BuildingRequestSearch;
import com.buildingweb.response.BuildingResponse;

public interface BuildingService {
    public Page<BuildingResponse> searchBuildingByBuildingRequest(BuildingRequestSearch buildingRequest,
            Pageable pageable);

    public void addNewBuilding(BuildingRequestAdd buildingRequest);

    public void updateBuilding(Long id, BuildingRequestAdd building);

    // public void deleteBuilding(Long id);
    public void deleteByListId(Long[] ids);

    public Page<BuildingResponse> findByNameContaining(String s, Pageable pageable);
}