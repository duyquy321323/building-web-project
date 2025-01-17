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
import com.buildingweb.request.ProfileEditRequest;
import com.buildingweb.request.RegisterRequest;
import com.buildingweb.response.LoginResponse;
import com.buildingweb.service.JwtService;
import com.buildingweb.service.UserService;
import com.buildingweb.utils.UtilFunction;

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
    public LoginResponse login(LoginRequest request, HttpServletRequest request2, HttpServletResponse response) {
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
                SecurityContextHolder.getContext().setAuthentication(authentication); // cho người dùng vào ngữ cảnh bảo
                                                                                      // mật hiện tại để từ các api khác
                                                                                      // có thể truy cập vào
                rememberMeServices.loginSuccess(request2, response, authentication); // nếu
                // có remember me
                String token = jwtService.generateToken(user); // tạo token cho user
                LoginResponse userLogin = userConverter.toLoginResponse(user);
                userLogin.setExpiryTime(jwtService.extractExpirationToken(token).getTime());
                userLogin.setToken(token);
                return userLogin;
            }
            throw new EntityNotFoundException("Account is not found");
        }
        throw new RequestNullException();
    }

    @Override
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
    @SneakyThrows
    public Page<UserDTO> getStaff(Pageable pageable, Long idBuilding, Long idCustomer) {
        return userRepository
                .findAllByRoleAndStatusAndIdBuildingOrIdCustomer(RoleConst.STAFF, 1, idBuilding, idCustomer, pageable)
                .map(it -> userConverter.toUserDTO(it));
    }

    @Override
    public void deliverTheBuilding(List<Long> id, Long buildingId) {

        Building building = buildingRepository.findById(buildingId)
                .orElseThrow(() -> new EntityNotFoundException("Building is not found"));
        if (building != null) {
            List<User> users = userRepository.findByIdInAndStatus(id, 1);
            List<User> staffs = new ArrayList<>();
            for (User user : users) {
                if (user.isStaff()) {
                    staffs.add(user);
                }
            }
            if (staffs == null || staffs.isEmpty())
                throw new EntityNotFoundException("Staff is not found");
            building.setUsers(staffs);
            buildingRepository.save(building);
            return;
        }
        throw new EntityNotFoundException("This building is not found");
    }

    @Override
    public void createAccount(CreateAccountRequest request) {
        User user = userConverter.createAccountToUser(request);
        userRepository.save(user);
    }

    @Override
    public void deleteAccount(List<Long> id) {
        List<User> users = userRepository.findByIdInAndStatus(id, 1);
        if (users != null && !users.isEmpty()) {
            for (User user : users) {
                user.setStatus(0);
            }
            userRepository.saveAll(users);
            return;
        }
        throw new EntityNotFoundException("User not found");
    }

    @Override
    public void editAccount(String username, List<RoleConst> roles, String fullname) {
        User user = userRepository.findByUsernameAndStatus(username, 1);
        if (user != null) {
            List<Role> roles2 = roles.stream().map(it -> roleRepository.findByCode(it))
                    .collect(Collectors.toList());
            if (UtilFunction.checkString(fullname)) {
                user.setFullname(fullname);
            }
            user.setRoles(roles2);
            userRepository.save(user);
            return;
        }
        throw new EntityNotFoundException("Account is not found");
    }

    @Override
    public void resetPassword(String username) {
        User user = userRepository.findByUsernameAndStatus(username, 1);
        if (user != null) {
            user.setPassword(passwordEncoder.encode(passwordLocal));
            userRepository.save(user);
            return;
        }
        throw new EntityNotFoundException("Account is not found");
    }

    @Override
    public void deliverTheCustomer(Long idCustomer, List<Long> idStaff) {
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

    @Override
    public List<UserDTO> getByFullname(String fullname, Long id) {
        return userRepository.findByFullnameContainingAndStatusAndIdNot(fullname, 1, id).stream()
                .map(it -> userConverter.toUserDTO(it))
                .collect(Collectors.toList());
    }

    @Override
    public LoginResponse editProfile(ProfileEditRequest request, Long id) {
        User user = userRepository.findByIdAndStatus(id, 1);
        if (user != null) {
            userRepository.save(userConverter.fromProfileEditRequest(request, user));
            return userConverter.toLoginResponse(user);
        } else
            throw new EntityNotFoundException("user not found");
    }
}