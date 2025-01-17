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
import com.buildingweb.request.ProfileEditRequest;
import com.buildingweb.request.RegisterRequest;
import com.buildingweb.response.LoginResponse;

public interface UserService {
    public LoginResponse login(LoginRequest request, HttpServletRequest request2, HttpServletResponse response);

    public void register(RegisterRequest request);

    public Page<UserDTO> getStaff(Pageable pageable, Long idBuilding, Long idCustomer);

    public void deliverTheBuilding(List<Long> id, Long buildingId);

    public void createAccount(CreateAccountRequest request);

    public void deleteAccount(List<Long> id);

    public void editAccount(String username, List<RoleConst> roles, String fullname);

    public void resetPassword(String username);

    public void deliverTheCustomer(Long idCustomer, List<Long> idStaff);

    public List<UserDTO> getByFullname(String fullname, Long id);

    public LoginResponse editProfile(ProfileEditRequest request, Long id);
}