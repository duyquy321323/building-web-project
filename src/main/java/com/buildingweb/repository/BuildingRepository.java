package com.buildingweb.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.buildingweb.entity.Building;
import com.buildingweb.repository.custom.BuildingRepositoryCustom;

public interface BuildingRepository extends JpaRepository<Building, Integer>, BuildingRepositoryCustom{
    public void deleteByIdIn(Integer[] ids); // xóa nhiều entity bằng list id cùng 1 lúc thêm hậu tố In vào
    public List<Building> findByNameContaining(String s); // tìm các building entity mà name có chứa chuỗi con s
}