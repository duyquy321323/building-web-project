package com.buildingweb.repository.custom;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.buildingweb.entity.User;

public interface UserRepositoryCustom {
    public Page<User> findAllByRoleAndStatus(String role, Integer status, Pageable pageable);
}