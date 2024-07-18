package com.buildingweb.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Value;
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
import com.buildingweb.entity.Customer;
import com.buildingweb.entity.Role;
import com.buildingweb.entity.User;
import com.buildingweb.enums.RoleConst;
import com.buildingweb.exception.custom.EntityAlreadyExistedException;
import com.buildingweb.exception.custom.EntityNotFoundException;
import com.buildingweb.exception.custom.PasswordNotMatchException;
import com.buildingweb.exception.custom.RequestNullException;
import com.buildingweb.model.UserDTO;
import com.buildingweb.repository.BuildingRepository;
import com.buildingweb.repository.CustomerRepository;
import com.buildingweb.repository.RoleRepository;
import com.buildingweb.repository.UserRepository;
import com.buildingweb.request.CreateAccountRequest;
import com.buildingweb.request.LoginRequest;
import com.buildingweb.request.RegisterRequest;
import com.buildingweb.service.JwtService;
import com.buildingweb.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RequiredArgsConstructor
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final RememberMeServices rememberMeServices;
    private final BuildingRepository buildingRepository;
    private final CustomerRepository customerRepository;
    private final RoleRepository roleRepository;

    @Value("${admin.account.password}")
    private String passwordLocal;

    @Override
    @SneakyThrows
    public UserDTO login(LoginRequest request, HttpServletRequest request2, HttpServletResponse response) {
        if (request != null) {
            User user = userRepository.findByUsernameAndStatus(request.getUsername(), 1);
            if (user != null) {
                if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) { // check pass
                    throw new PasswordNotMatchException();
                }
                UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken( // tạo
                                                                                                                   // đối
                                                                                                                   // tượng
                                                                                                                   // yêu
                                                                                                                   // cầu
                                                                                                                   // xác
                                                                                                                   // thực
                        request.getUsername(), request.getPassword(), userDetails.getAuthorities());
                Authentication authentication = authenticationManager.authenticate(authenticationToken);// cho yêu cầu
                                                                                                        // đấy
                                                                                                        // vào
                                                                                                        // đối tượng
                                                                                                        // quản lý
                                                                                                        // xác
                SecurityContextHolder.getContext().setAuthentication(authentication);
                rememberMeServices.loginSuccess(request2, response, authentication);
                String token = jwtService.generateToken(user); // tạo token cho user
                UserDTO userDTO = userConverter.toUserDTO(user);
                userDTO.setToken(token);
                return userDTO;
            }
            throw new EntityNotFoundException("Account is not found");
        }
        throw new RequestNullException();
    }

    @Override
    @SneakyThrows
    public void register(RegisterRequest request) {
        if (request != null) {
            if (request.getPassword().equals(request.getConfirmPassword()) == false) {
                throw new PasswordNotMatchException("Confirm password is not match!");
            }
            User user = userRepository.findByUsernameAndStatus(request.getUsername(), 1);
            if (user != null) {
                throw new EntityAlreadyExistedException();
            }
            user = userConverter.registerRequestToUser(request);
            userRepository.save(user);
            return;
        }
        throw new RequestNullException();
    }

    @Override
    public Page<UserDTO> getAllStaff(Pageable pageable) {
        return userRepository.findAllByRoleAndStatus(RoleConst.STAFF, 1, pageable)
                .map(it -> userConverter.toUserDTO(it));
    }

    @Override
    public void deliverTheBuilding(List<Long> id, Long buildingId) {

        if (buildingId != null && id != null && !id.isEmpty()) {
            Building building = buildingRepository.findById(buildingId).get();
            if (building != null) {
                List<User> users = userRepository.findByIdInAndStatus(id, 1);
                List<User> staffs = new ArrayList<>();
                for (User user : users) {
                    if (user.isStaff()) {
                        staffs.add(user);
                    }
                }
                building.setUsers(staffs);
                buildingRepository.save(building);
                return;
            }
            throw new EntityNotFoundException("This building is not found");
        }
        throw new RequestNullException();
    }

    @Override
    public void createAccount(CreateAccountRequest request) {
        if (request != null) {
            User user = userConverter.createAccountToUser(request);
            userRepository.save(user);
            return;
        }
        throw new RequestNullException();
    }

    @Override
    public void deleteAccount(List<Long> id) {
        if (id != null) {
            List<User> users = userRepository.findByIdInAndStatus(id, 1);
            if (users != null) {
                for (User user : users) {
                    user.setStatus(0);
                }
                userRepository.saveAll(users);
                return;
            }
            throw new EntityNotFoundException();
        }
        throw new RequestNullException();
    }

    @Override
    public void editAccount(String username, List<RoleConst> roles, String fullname) {
        if (username != null && roles != null && fullname != null) {
            User user = userRepository.findByUsernameAndStatus(username, 1);
            if (user != null) {
                List<Role> roles2 = roles.stream().map(it -> roleRepository.findByCode(it))
                        .collect(Collectors.toList());
                user.setRoles(roles2);
                user.setFullname(fullname);
                userRepository.save(user);
                return;
            }
            throw new EntityNotFoundException("Account is not found");
        }
        throw new RequestNullException();
    }

    @Override
    public void resetPassword(String username) {
        if (username != null) {
            User user = userRepository.findByUsernameAndStatus(username, 1);
            if (user != null) {
                user.setPassword(passwordEncoder.encode(passwordLocal));
                userRepository.save(user);
                return;
            }
            throw new EntityNotFoundException("Account is not found");
        }
        throw new RequestNullException();
    }

    @Override
    public void deliverTheCustomer(Long idCustomer, List<Long> idStaff) {
        if (idCustomer != null && idStaff != null && !idStaff.isEmpty()) {
            Customer customer = customerRepository.findByIdAndIsActive(idCustomer, 1);
            if (customer != null) {
                List<User> users = userRepository.findByIdInAndStatus(idStaff, 1);
                List<User> staffs = new ArrayList<>();
                for (User user : users) {
                    if (user.isStaff()) {
                        staffs.add(user);
                    }
                }
                customer.setUsers(staffs);
                customerRepository.save(customer);
                return;
            }
            throw new EntityNotFoundException("Customer is not found");
        }
        throw new RequestNullException();
    }
}