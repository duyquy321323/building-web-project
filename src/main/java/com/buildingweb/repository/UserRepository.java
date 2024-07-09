package com.buildingweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.buildingweb.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    public User findByUsernameAndStatus(String username, Integer status);

    public User findByUsername(String username);
}