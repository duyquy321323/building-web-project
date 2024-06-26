package com.buildingweb.service;

import java.util.List;

import com.buildingweb.request.BuildingRequest;
import com.buildingweb.response.BuildingResponse;

public interface BuildingService {
    public List<BuildingResponse> searchBuildingByBuildingRequest(BuildingRequest buildingRequest);
}