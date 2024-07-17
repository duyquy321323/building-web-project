package com.buildingweb.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

import com.buildingweb.filter.JwtTokenFilter;
import com.buildingweb.security.CustomUserDetailsSevice;

import org.springframework.security.config.Customizer;

import lombok.RequiredArgsConstructor;

@SuppressWarnings("deprecation")
@Configuration
@RequiredArgsConstructor
@EnableWebSecurity // đánh dấu rằng lớp này là lớp cấu hình bảo mật
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${key.remember-me}")
    private String rememberMeKey;

    @Bean
    @Override
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

    @Bean
    public RememberMeServices rememberMeServices() {
        TokenBasedRememberMeServices tokenBasedRememberMeServices = new TokenBasedRememberMeServices(secret,
                userDetailsService());
        tokenBasedRememberMeServices.setParameter("remember-me");
        tokenBasedRememberMeServices.setTokenValiditySeconds(500);
        return tokenBasedRememberMeServices;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeRequests(requests -> requests
                        .antMatchers(HttpMethod.POST, "/login", "/register", "/logout",
                                "/buildings/search")
                        .permitAll()
                        .antMatchers(HttpMethod.GET, "/swagger-ui/**",
                                "/v3/api-docs/**", "/v3/api-docs", "/API license URL")
                        .permitAll()
                        .antMatchers(HttpMethod.POST, "/buildings/new").hasAnyRole("MANAGER", "STAFF")
                        .antMatchers(HttpMethod.PUT, "/buildings/").hasAnyRole("MANAGER", "STAFF")
                        .antMatchers(HttpMethod.DELETE, "/buildings/").hasRole("MANAGER")
                        .antMatchers(HttpMethod.GET, "/staffs").hasRole("MANAGER")
                        .antMatchers(HttpMethod.POST, "/buildings/users").hasRole("MANAGER")
                        .anyRequest().denyAll())
                .rememberMe(rm -> rm.rememberMeServices(
                        rememberMeServices()).key(rememberMeKey))
                .addFilterBefore(jwtTokenFilter(),
                        UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout.deleteCookies("JSESSIONID", "remember-me").permitAll());
    }

}