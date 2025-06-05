package com.pennywise.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Runs for every request under /api/** (except login/register).
 * Checks for a Bearer token in Authorization header, validates it,
 * and if valid, sets the authenticated user in Spring’s SecurityContext.
 */
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        // Expect header in the form: "Bearer <token>"
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            // Validate token
            if (jwtUtil.validateToken(token)) {
                // Get username (email) from token
                String username = jwtUtil.getUsernameFromJwt(token);
                // Load UserDetails to build Authentication object
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // Create an authenticated UsernamePasswordAuthenticationToken
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());
                // Attach details (optional) and set in context
                authentication.setDetails(userDetails);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        // Continue filter chain (whether authenticated or not)
        filterChain.doFilter(request, response);
    }

    /**
     * We want this filter to run for every path except the login and register
     * endpoints,
     * so we’ll configure that in SecurityConfig with
     * antMatchers/authorizeHttpRequests.
     */
}
