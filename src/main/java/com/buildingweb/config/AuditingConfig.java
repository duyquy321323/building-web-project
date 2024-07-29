package com.buildingweb.config;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider") // cho phép tự động điền các thông tin về người dùng hiện tại
                                                        // xuống các annotation createdDate, By, ...
public class AuditingConfig {
    @Bean
    public AuditorAware<String> auditorProvider() {
        return new SpringSecurityAuditAwareImpl();
    }

}

class SpringSecurityAuditAwareImpl implements AuditorAware<String> {
    @SuppressWarnings("null")
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); // lấy yêu cầu xác thực
                                                                                                // của người dùng hiện
                                                                                                // tại ra
        if (authentication == null || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) { // kiểm tra xem đã được xác thực hay chưa
            return Optional.of("anonymousUser");
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal(); // lấy thông tin của người dùng ra nếu đã
                                                                               // được xác thực
        return Optional.of(userDetails.getUsername()); // trả về tên người dùng
    }
}