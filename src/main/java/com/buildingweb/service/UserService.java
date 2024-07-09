package com.buildingweb.service;

import com.buildingweb.model.UserDTO;
import com.buildingweb.request.LoginRequest;
import com.buildingweb.request.RegisterRequest;

public interface UserService {
    public UserDTO login(LoginRequest request);

    public void register(RegisterRequest request);
}