package com.buildingweb.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.buildingweb.entity.Role;
import com.buildingweb.entity.User;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CustomUserDetails implements UserDetails {

    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    public static UserDetails build(User user) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role : user.getRoles()) {
            if (role.getCode().equals("MANAGER")) {
                authorities.add(new SimpleGrantedAuthority("ROLE_MANAGER"));
            } else if (role.getCode().equals("STAFF")) {
                authorities.add(new SimpleGrantedAuthority("ROLE_STAFF"));
            }
        }
        return new CustomUserDetails(user.getUsername(), user.getPassword(), authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}