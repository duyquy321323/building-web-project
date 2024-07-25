package com.buildingweb.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.buildingweb.enums.RoleConst;
import com.buildingweb.model.UserDTO;
import com.buildingweb.request.CreateAccountRequest;
import com.buildingweb.request.LoginRequest;
import com.buildingweb.request.RegisterRequest;

public interface UserService {
    public UserDTO login(LoginRequest request, HttpServletRequest request2, HttpServletResponse response);

    public void register(RegisterRequest request);

    public Page<UserDTO> getStaff(Pageable pageable, Long idBuilding);

    public void deliverTheBuilding(List<Long> id, Long buildingId);

    public void createAccount(CreateAccountRequest request);

    public void deleteAccount(List<Long> id);

    public void editAccount(String username, List<RoleConst> roles, String fullname);

    public void resetPassword(String username);

    public void deliverTheCustomer(Long idCustomer, List<Long> idStaff);
}