package com.buildingweb.service.impl;

import org.springframework.stereotype.Service;

import com.buildingweb.entity.Role;
import com.buildingweb.enums.RoleConst;
import com.buildingweb.exception.custom.RequestNullException;
import com.buildingweb.repository.RoleRepository;
import com.buildingweb.service.RoleService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role getRoleByCode(RoleConst role) {
        if (role != null) {
            return roleRepository.findByCode(role);
        }
        throw new RequestNullException("role request is null");
    }
}