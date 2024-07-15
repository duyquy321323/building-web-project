package com.buildingweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.buildingweb.entity.Building;
import com.buildingweb.repository.custom.BuildingRepositoryCustom;

public interface BuildingRepository extends JpaRepository<Building, Long>, BuildingRepositoryCustom {
    public void deleteByIdIn(@Param("ids") Long[] ids); // xóa nhiều entity bằng list id cùng 1

    public Page<Building> findByNameContaining(String s, Pageable pageable); // tìm các building entity mà name có chứa
                                                                             // chuỗi con s
}