package com.helpysoft.mima_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
public class AuditConfig {

    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> {
            try {
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();

                if (auth == null || !auth.isAuthenticated()) {
                    return Optional.of("SYSTEM");
                }

                Object principal = auth.getPrincipal();

                // Si principal est un UserDetails
                if (principal instanceof CustomUserDetails userDetails) {
                    return Optional.of(userDetails.getUsername());
                }

                // Si principal est juste un username
                if (principal instanceof String username) {
                    return Optional.of(username);
                }

                return Optional.of("SYSTEM");

            } catch (Exception e) {
                return Optional.of("SYSTEM");
            }
        };
    }
}
