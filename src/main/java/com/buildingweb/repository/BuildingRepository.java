package com.buildingweb.repository;

import java.util.List;

import com.buildingweb.entity.Building;
import com.buildingweb.request.BuildingRequest;

public interface BuildingRepository{
    public List<Building> findBuildingByBuildingRequest(BuildingRequest buildingRequest);
}