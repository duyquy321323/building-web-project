package com.buildingweb.service.impl;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.buildingweb.converter.UserConverter;
import com.buildingweb.entity.User;
import com.buildingweb.exception.custom.EntityAlreadyExistedException;
import com.buildingweb.exception.custom.PasswordNotMatchException;
import com.buildingweb.model.UserDTO;
import com.buildingweb.repository.UserRepository;
import com.buildingweb.request.LoginRequest;
import com.buildingweb.request.RegisterRequest;
import com.buildingweb.service.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserConverter userConverter;

    @Override
    public UserDTO login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername());
        if (!BCrypt.checkpw(request.getPassword(), user.getPassword())) {
            throw new PasswordNotMatchException();
        }
        return userConverter.toUserDTO(user);
    }

    @Override
    public void register(RegisterRequest request) {
        User user = userRepository.findByUsername(request.getUsername());
        if (user != null) {
            throw new EntityAlreadyExistedException();
        }
        user = userConverter.registerRequestToUser(request);
        userRepository.save(user);
    }
}