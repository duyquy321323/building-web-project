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

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameAndStatus(username, 1);
        if (user == null) {
            throw new UsernameNotFoundException("Username not found!");
        }
        return CustomUserDetails.build(user);
    }
}