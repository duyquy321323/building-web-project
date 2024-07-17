package com.buildingweb.service.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.stereotype.Service;

import com.buildingweb.converter.UserConverter;
import com.buildingweb.entity.Building;
import com.buildingweb.entity.User;
import com.buildingweb.exception.custom.EntityAlreadyExistedException;
import com.buildingweb.exception.custom.NotAllowRoleException;
import com.buildingweb.exception.custom.PasswordNotMatchException;
import com.buildingweb.model.UserDTO;
import com.buildingweb.repository.BuildingRepository;
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
    private final RememberMeServices rememberMeServices;
    private final BuildingRepository buildingRepository;

    @Override
    @SneakyThrows
    public UserDTO login(LoginRequest request, HttpServletRequest request2, HttpServletResponse response) {
        User user = userRepository.findByUsername(request.getUsername());
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) { // check pass
            throw new PasswordNotMatchException();
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken( // tạo đối
                                                                                                           // tượng yêu
                                                                                                           // cầu xác
                                                                                                           // thực
                request.getUsername(), request.getPassword(), userDetails.getAuthorities());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);// cho yêu cầu đấy vào
                                                                                                // đối tượng quản lý xác
        SecurityContextHolder.getContext().setAuthentication(authentication);
        rememberMeServices.loginSuccess(request2, response, authentication);
        String token = jwtService.generateToken(user); // tạo token cho user
        UserDTO userDTO = userConverter.toUserDTO(user);
        userDTO.setToken(token);
        return userDTO;
    }

    @Override
    @SneakyThrows
    public void register(RegisterRequest request) {
        if (request.getPassword().equals(request.getConfirmPassword()) == false) {
            throw new PasswordNotMatchException("Confirm password is not match!");
        }
        User user = userRepository.findByUsername(request.getUsername());
        if (user != null) {
            throw new EntityAlreadyExistedException();
        }
        user = userConverter.registerRequestToUser(request);
        userRepository.save(user);
    }

    @Override
    public Page<UserDTO> getAllStaff(Pageable pageable) {
        return userRepository.findAllByRoleAndStatus("STAFF", 1, pageable)
                .map(it -> userConverter.toUserDTO(it));
    }

    @Override
    public void deliverTheBuilding(List<Long> id, Long buildingId) {

        for (Long userId : id) {
            if (!userRepository.findById(userId).get().isStaff()) {
                throw new NotAllowRoleException();
            }
        }

        Building building = buildingRepository.findById(buildingId).get();
        if (building == null)
            return;
        List<User> users = userRepository.findByIdIn(id);
        building.setUsers(users);
        buildingRepository.save(building);
    }
}