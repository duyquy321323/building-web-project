package com.buildingweb.service;

import java.util.List;

import com.buildingweb.request.BuildingRequestAdd;
import com.buildingweb.request.BuildingRequestSearch;
import com.buildingweb.response.BuildingResponse;

public interface BuildingService {
    public List<BuildingResponse> searchBuildingByBuildingRequest(BuildingRequestSearch buildingRequest);
    public void addNewBuilding(BuildingRequestAdd buildingRequest);
    public void updateBuilding(Long id, BuildingRequestAdd building);
    // public void deleteBuilding(Long id);
    public void deleteByListId(Long[] ids);
    public List<BuildingResponse> findByNameContaining(String s);
}