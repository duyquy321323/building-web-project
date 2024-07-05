package com.buildingweb.repository.custom;

import java.util.List;

import com.buildingweb.entity.Building;
import com.buildingweb.request.BuildingRequestSearch;

public interface BuildingRepositoryCustom {
    public List<Building> findByBuildingRequestSearch(BuildingRequestSearch buildingRequest);
}