package com.buildingweb.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.buildingweb.model.UserDTO;
import com.buildingweb.request.LoginRequest;
import com.buildingweb.request.RegisterRequest;

public interface UserService {
    public UserDTO login(LoginRequest request, HttpServletRequest request2, HttpServletResponse response);

    public void register(RegisterRequest request);

    public Page<UserDTO> getAllStaff(Pageable pageable);

    public void deliverTheBuilding(List<Long> id, Long buildingId);
}