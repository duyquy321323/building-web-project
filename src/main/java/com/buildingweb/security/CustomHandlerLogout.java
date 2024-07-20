package com.buildingweb.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import com.buildingweb.service.BlackListService;

import lombok.NoArgsConstructor;

@NoArgsConstructor
@Primary
public class CustomHandlerLogout implements LogoutHandler {

    @Autowired
    private BlackListService blackListService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String authenticationHeader = request.getHeader("Authorization");

        if (authenticationHeader != null && authenticationHeader.startsWith("Bearer ")) {
            String token = authenticationHeader.substring(7);
            blackListService.addToBlackList(token);
        }
    }
}