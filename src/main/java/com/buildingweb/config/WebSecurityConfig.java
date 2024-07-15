package com.buildingweb.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

import com.buildingweb.filter.JwtTokenFilter;
import com.buildingweb.security.CustomUserDetailsSevice;

import org.springframework.security.config.Customizer;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity // đánh dấu rằng lớp này là lớp cấu hình bảo mật
public class WebSecurityConfig {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${key.remember-me}")
    private String rememberMeKey;

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
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration authenticationConfiguration)
            throws Exception { // tạo đối tượng authenticationManager
        // để xử lý xác thực
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public JwtTokenFilter jwtTokenFilter() { // tạo đối tượng JwtTokenFilter kế thừa class OncePerRequestFilter để tạo 1
                                             // lớp filter token
        return new JwtTokenFilter();
    }

    @Bean
    public RememberMeServices rememberMeServices() {
        TokenBasedRememberMeServices tokenBasedRememberMeServices = new TokenBasedRememberMeServices(secret,
                userDetailsService());
        tokenBasedRememberMeServices.setParameter("remember-me");
        tokenBasedRememberMeServices.setTokenValiditySeconds(500);
        return tokenBasedRememberMeServices;
    }

    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .authorizeRequests(requests -> requests
                        .antMatchers(HttpMethod.POST, "/login", "/register", "/logout").permitAll()
                        .antMatchers(HttpMethod.GET, "/buildings/", "/swagger-ui/**", "/swagger-config.js",
                                "/v3/api-docs/**", "/v3/api-docs")
                        .permitAll()
                        .antMatchers(HttpMethod.POST, "/buildings/").hasAnyRole("MANAGER", "STAFF")
                        .antMatchers(HttpMethod.PUT, "/buildings/").hasAnyRole("MANAGER", "STAFF")
                        .antMatchers(HttpMethod.DELETE, "/buildings/").hasRole("MANAGER")
                        .antMatchers(HttpMethod.GET, "/staffs").hasRole("MANAGER")
                        .antMatchers(HttpMethod.POST, "/buildings/users").hasRole("MANAGER")
                        .anyRequest().denyAll())
                .rememberMe(rm -> rm.rememberMeServices(
                        rememberMeServices()).key(rememberMeKey))
                .addFilterBefore(jwtTokenFilter(),
                        UsernamePasswordAuthenticationFilter.class)
                .logout(lo -> lo.deleteCookies("JSESSIONID", "remember-me").permitAll());
        http.authenticationProvider(authenticationProvider());
        return http.build();
    }

}