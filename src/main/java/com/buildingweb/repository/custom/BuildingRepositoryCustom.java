package com.buildingweb.repository.custom;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.buildingweb.entity.Building;
import com.buildingweb.request.BuildingRequestSearch;

public interface BuildingRepositoryCustom {
    public Page<Building> findByBuildingRequestSearch(BuildingRequestSearch buildingRequest, Pageable pageable);
}