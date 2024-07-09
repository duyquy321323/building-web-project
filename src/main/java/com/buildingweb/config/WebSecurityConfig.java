package com.buildingweb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.buildingweb.security.CustomUserDetailsSevice;

@Configuration
@EnableWebSecurity // đánh dấu rằng lớp này là lớp cấu hình bảo mật
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    public UserDetailsService userDetailsService() { // lấy thông tin người dùng
        return new CustomUserDetailsSevice();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() { // mã hóa mật khẩu
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService()); // lấy thông tin người dùng
        authProvider.setPasswordEncoder(passwordEncoder()); // so sánh mật khẩu
        return authProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider()); // cấu hình inject đối tượng DAO đó vào
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable() // tắt chế độ chặn tấn công csrf
                .authorizeRequests() // tạo quy tắc các yêu cầu api
                .antMatchers("/api/**").hasRole("MANAGER")
                .antMatchers("/login", "/register").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().permitAll();
    }
}