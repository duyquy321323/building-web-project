package com.buildingweb.converter;

import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.buildingweb.entity.User;
import com.buildingweb.model.UserDTO;
import com.buildingweb.request.CreateAccountRequest;
import com.buildingweb.request.RegisterRequest;
import com.buildingweb.response.LoginResponse;
import com.buildingweb.service.RoleService;

@Component
public class UserConverter {

    @Value("${admin.account.password}")
    private String password;

    @Autowired
    private RoleService roleService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public LoginResponse toLoginResponse(User user) {
        LoginResponse userLogin = modelMapper.map(user, LoginResponse.class);
        userLogin.setRoles(user.getRoles().stream().map(it -> it.getCode()).collect(Collectors.toList()));
        return userLogin;
    }

    public User registerRequestToUser(RegisterRequest request) {
        User user = modelMapper.map(request, User.class);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setStatus(1);
        return user;
    }

    public User createAccountToUser(CreateAccountRequest request) {
        User user = modelMapper.map(request, User.class);
        user.setPassword(passwordEncoder.encode(password));
        user.setStatus(1);
        user.setRoles(
                request.getRoles().stream().distinct().map(it -> roleService.getRoleByCode(it))
                        .collect(Collectors.toList()));
        return user;
    }

    public UserDTO toUserDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }
}