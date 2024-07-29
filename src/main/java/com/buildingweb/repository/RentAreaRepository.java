package com.buildingweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.buildingweb.entity.RentArea;

public interface RentAreaRepository extends JpaRepository<RentArea, Long> {
}