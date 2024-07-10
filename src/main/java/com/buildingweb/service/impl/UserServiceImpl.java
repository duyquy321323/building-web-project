package com.buildingweb.service.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.buildingweb.converter.UserConverter;
import com.buildingweb.entity.User;
import com.buildingweb.exception.custom.EntityAlreadyExistedException;
import com.buildingweb.exception.custom.PasswordNotMatchException;
import com.buildingweb.model.UserDTO;
import com.buildingweb.repository.UserRepository;
import com.buildingweb.request.LoginRequest;
import com.buildingweb.request.RegisterRequest;
import com.buildingweb.service.JwtService;
import com.buildingweb.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @SneakyThrows
    public UserDTO login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername());
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new PasswordNotMatchException();
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails.getUsername(), request.getPassword(), userDetails.getAuthorities());
        authenticationManager.authenticate(authenticationToken);
        String token = jwtService.generateToken(user);
        UserDTO userDTO = userConverter.toUserDTO(user);
        userDTO.setToken(token);
        return userDTO;
    }

    @Override
    @SneakyThrows
    public void register(RegisterRequest request) {
        User user = userRepository.findByUsername(request.getUsername());
        if (user != null) {
            throw new EntityAlreadyExistedException();
        }
        user = userConverter.registerRequestToUser(request);
        userRepository.save(user);
    }
}