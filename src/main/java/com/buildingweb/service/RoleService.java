package com.buildingweb.service;

import com.buildingweb.entity.Role;
import com.buildingweb.enums.RoleConst;

public interface RoleService {
    public Role getRoleByCode(RoleConst role);
}