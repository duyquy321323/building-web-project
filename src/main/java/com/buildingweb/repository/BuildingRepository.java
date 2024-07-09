package com.buildingweb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.Modifying;
// import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.buildingweb.entity.Building;
import com.buildingweb.repository.custom.BuildingRepositoryCustom;

public interface BuildingRepository extends JpaRepository<Building, Long>, BuildingRepositoryCustom {
    // @Modifying
    // @Query("DELETE FROM Building u WHERE u.id IN :ids")
    public void deleteByIdIn(@Param("ids") Long[] ids); // xóa nhiều entity bằng list id cùng 1
    // lúc thêm hậu tố In vào

    public List<Building> findByNameContaining(String s); // tìm các building entity mà name có chứa chuỗi con s
}