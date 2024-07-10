package com.buildingweb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.buildingweb.filter.JwtTokenFilter;
import com.buildingweb.security.CustomUserDetailsSevice;

@Configuration
@EnableWebSecurity // đánh dấu rằng lớp này là lớp cấu hình bảo mật
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    public UserDetailsService userDetailsService() { // hàm cung cấp cách lấy thông tin người dùng
        return new CustomUserDetailsSevice();
    }

    @Bean
    public PasswordEncoder passwordEncoder() { // mã hóa mật khẩu
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() { // tạo đối tượng DAO để lưu cách lấy thông tin người
                                                                // dùng và cách mã hóa mật khẩu
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService()); // lấy thông tin người dùng
        authProvider.setPasswordEncoder(passwordEncoder()); // mã hóa mật khẩu
        return authProvider;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception { // tạo đối tượng authenticationManager
                                                                                // để xử lý xác thực
        return super.authenticationManagerBean();
    }

    @Bean
    public JwtTokenFilter jwtTokenFilter() { // tạo đối tượng JwtTokenFilter kế thừa class OncePerRequestFilter để tạo 1
                                             // lớp filter token
        return new JwtTokenFilter();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider()); // cấu hình inject đối tượng DAO đó vào web
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception { // cấu hình phân quyền web
        http.csrf().disable() // tắt tính năng chặn tấn công csrf khi đã có token
                .addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class) // thêm 1 lớp filter
                                                                                               // token trước lớp phân
                                                                                               // quyền
                .authorizeRequests() // tạo quy tắc phân quyền
                .antMatchers("/account/**").permitAll() // cho phép tất cả mọi người vào được
                .antMatchers("/buildings/**").hasAnyRole("MANAGER") // chỉ cho phép MANAGER vào api này
                .anyRequest().authenticated(); // tất cả các api đã xác thực mới được vào
    }
}