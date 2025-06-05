package com.pennywise.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;

/**
 * Utility class for generating and validating JWTs.
 * Reads secret key and expiration time from application properties.
 */
@Component
public class JwtUtil {

    // Injected from application-postgres.properties (or application.yml)
    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration-ms}")
    private long jwtExpirationMs;

    private Key key; // Parsed signing key

    @PostConstruct
    public void init() {
        // Decode the base64-encoded secret into a Key instance
        // You must ensure that jwtSecret is at least 32 characters long
        key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    /**
     * Generate a JWT token containing the user's email as the subject.
     * 
     * @param username or email
     * @return signed JWT string
     */
    public String generateToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Validate the token’s signature and expiration.
     *
     * @param token raw JWT string
     * @return true if valid, false otherwise
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            // Any parsing or signature exception means token is invalid
            return false;
        }
    }

    /**
     * Extract the username (email) stored in the subject claim.
     *
     * @param token raw JWT string
     * @return subject (the username/email)
     * @throws JwtException if parsing fails
     */
    public String getUsernameFromJwt(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
}
