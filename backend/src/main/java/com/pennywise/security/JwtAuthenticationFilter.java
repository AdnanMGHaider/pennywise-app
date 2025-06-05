package com.pennywise.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Intercepts POST /api/auth/login requests. Reads JSON body with "email" and
 * "password",
 * attempts authentication, and if successful, returns a JWT in the response
 * body.
 */
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;

        // By default, UsernamePasswordAuthenticationFilter listens to "/login" POST.
        // We want it to handle "/api/auth/login" instead:
        setFilterProcessesUrl("/api/auth/login");
    }

    /**
     * Attempt to authenticate when credentials arrive.
     * Reads { "email": "...", "password": "..." } from request body.
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
            HttpServletResponse response) throws AuthenticationException {
        try {
            // Parse JSON from request input stream into a map
            Map<String, String> creds = new ObjectMapper()
                    .readValue(request.getInputStream(), Map.class);
            String email = creds.get("email");
            String password = creds.get("password");

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password);
            // Delegate to AuthenticationManager (which in turn calls
            // CustomUserDetailsService)
            return authenticationManager.authenticate(authToken);
        } catch (IOException ex) {
            throw new RuntimeException("Failed to parse authentication request body", ex);
        }
    }

    /**
     * If authentication succeeds, generate a JWT and return it in the response body
     * as JSON.
     * e.g. { "token": "eyJhbGciOiJIUzI1NiIs..." }
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
        // authResult.getName() is the username (email) returned by UserDetailsService
        String username = authResult.getName();
        String token = jwtUtil.generateToken(username);

        // Return the token in JSON form
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        new ObjectMapper().writeValue(response.getWriter(), tokenMap);
    }

    /**
     * If authentication fails (bad credentials), Spring Security will invoke this
     * method.
     * By default, it sends a 401 response. We can override for custom JSON error
     * messages,
     * but we’ll let the default 401 Unauthorized stand.
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"Invalid email or password\"}");
    }
}
