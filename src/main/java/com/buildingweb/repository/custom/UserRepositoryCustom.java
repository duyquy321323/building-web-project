package com.buildingweb.repository.custom;

import java.sql.SQLException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.buildingweb.entity.User;
import com.buildingweb.enums.RoleConst;

public interface UserRepositoryCustom {
    public Page<User> findAllByRoleAndStatusAndIdBuildingOrIdCustomer(RoleConst role, Integer status, Long idBuilding,
            Long idCustomer,
            Pageable pageable) throws SQLException;
}