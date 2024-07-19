package com.buildingweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.buildingweb.entity.Role;
import com.buildingweb.enums.RoleConst;

public interface RoleRepository extends JpaRepository<Role, Long> {
    public Role findByCode(RoleConst role);
}