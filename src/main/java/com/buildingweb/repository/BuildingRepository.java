package com.buildingweb.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.buildingweb.entity.Building;

public interface BuildingRepository extends JpaRepository<Building, Integer>{
}