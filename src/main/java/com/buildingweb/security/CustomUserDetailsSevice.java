package com.buildingweb.security;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.buildingweb.entity.User;
import com.buildingweb.repository.UserRepository;

import lombok.NoArgsConstructor;

@NoArgsConstructor
@Transactional
public class CustomUserDetailsSevice implements UserDetailsService {

    @Autowired(required = false)
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // hàm lấy ra chi tiết người dùng từ username
        User user = userRepository.findByUsernameAndStatus(username, 1); // lấy user có status là 1 và có username
        if (user == null) { // nếu không có thì tài khoản không tồn tại
            throw new UsernameNotFoundException("Username not found!");
        }
        return CustomUserDetails.build(user); // nếu có thì build UserDetails
    }
}