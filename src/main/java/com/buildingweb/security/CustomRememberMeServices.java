package com.buildingweb.security;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

public class CustomRememberMeServices extends TokenBasedRememberMeServices {

    public CustomRememberMeServices(String key, UserDetailsService userDetailsService) {
        super(key, userDetailsService);
    }

    @Override
    protected void setCookie(String[] tokens, int maxAge, HttpServletRequest request, HttpServletResponse response) {
        // Tạo cookie mới với thuộc tính HttpOnly = false
        Cookie cookie = new Cookie(SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY, encodeCookie(tokens));
        cookie.setMaxAge(maxAge);
        cookie.setPath("/");
        cookie.setSecure(request.isSecure());
        cookie.setHttpOnly(false); // Bỏ thuộc tính HttpOnly

        response.addCookie(cookie);
    }
}
