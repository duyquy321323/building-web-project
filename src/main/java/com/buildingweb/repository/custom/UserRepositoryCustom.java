package com.buildingweb.repository.custom;

import java.sql.SQLException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.buildingweb.entity.User;
import com.buildingweb.enums.RoleConst;

public interface UserRepositoryCustom {
    public Page<User> findAllByRoleAndStatusAndIdBuilding(RoleConst role, Integer status, Long idBuilding,
            Pageable pageable) throws SQLException;
}