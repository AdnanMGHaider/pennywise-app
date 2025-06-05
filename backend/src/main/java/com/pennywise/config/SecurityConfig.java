package com.pennywise.config;

import com.pennywise.security.CustomUserDetailsService;
import com.pennywise.security.JwtAuthenticationFilter;
import com.pennywise.security.JwtAuthorizationFilter;
import com.pennywise.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Central Spring Security configuration (Spring Security 6 DSL).
 * • Exposes AuthenticationManager (wired to your CustomUserDetailsService).
 * • Disables CSRF, enforces stateless JWT sessions.
 * • Permits /api/auth/register and /api/auth/login; protects all other /api/**
 * endpoints.
 * • Registers JwtAuthenticationFilter and JwtAuthorizationFilter in the correct
 * order.
 */
@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    @Autowired
    public SecurityConfig(CustomUserDetailsService userDetailsService,
            JwtUtil jwtUtil) {
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Expose the AuthenticationManager bean so that JwtAuthenticationFilter can use
     * it.
     * Spring Boot 3 / Security 6 require this method instead of extending
     * WebSecurityConfigurerAdapter.
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 1) Prepare the authentication filter for /api/auth/login
        JwtAuthenticationFilter authFilter = new JwtAuthenticationFilter(
                authenticationManager(http.getSharedObject(AuthenticationConfiguration.class)),
                jwtUtil);
        authFilter.setFilterProcessesUrl("/api/auth/login");

        // 2) Prepare the authorization filter for all other /api/** requests
        JwtAuthorizationFilter authorizationFilter = new JwtAuthorizationFilter(
                jwtUtil,
                userDetailsService);

        http
                // 3) Disable CSRF entirely (we’re using stateless JWTs)
                .csrf(AbstractHttpConfigurer::disable)

                // 4) Make sure we don’t use HTTP sessions; every request is stateless
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 5) Define which endpoints are public and which require a JWT
                .authorizeHttpRequests(auth -> auth
                        // Allow anyone to hit /api/auth/register and /api/auth/login
                        .requestMatchers("/api/auth/register", "/api/auth/login")
                        .permitAll()
                        // Any other endpoint under /api/** must be authenticated
                        .requestMatchers("/api/**")
                        .authenticated()
                        // Everything else (e.g. static resources) is still public
                        .anyRequest()
                        .permitAll())

                // 6) Register our custom filters:
                // - First, authenticate users (login) with JwtAuthenticationFilter
                // - Then, check JWT on every other request with JwtAuthorizationFilter
                .addFilter(authFilter)
                .addFilterBefore(
                        authorizationFilter,
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
