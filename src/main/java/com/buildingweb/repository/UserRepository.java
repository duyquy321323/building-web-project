package com.buildingweb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.buildingweb.entity.User;
import com.buildingweb.repository.custom.UserRepositoryCustom;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
    public User findByUsernameAndStatus(String username, Integer status);

    public List<User> findByFullnameContainingAndStatus(String fullname, Integer status);

    public List<User> findByIdInAndStatus(List<Long> userIds, Integer status);
}