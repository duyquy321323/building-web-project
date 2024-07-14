package com.buildingweb.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.buildingweb.model.UserDTO;
import com.buildingweb.request.LoginRequest;
import com.buildingweb.request.RegisterRequest;

public interface UserService {
    public UserDTO login(LoginRequest request, HttpServletRequest request2, HttpServletResponse response);

    public void register(RegisterRequest request);
}