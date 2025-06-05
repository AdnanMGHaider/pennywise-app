package com.pennywise.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Exposes a PasswordEncoder bean so other beans (like UserService)
 * can @Autowired a PasswordEncoder to hash and verify passwords.
 */
@Configuration
public class PasswordConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        // bcrypt is the industry standard for password hashing
        return new BCryptPasswordEncoder();
    }
}
