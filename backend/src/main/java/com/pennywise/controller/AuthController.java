package com.pennywise.controller;

import com.pennywise.model.User;
import com.pennywise.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest req) {
        try {
            User saved = userService.registerUser(req.getEmail(), req.getPassword());
            return ResponseEntity.status(HttpStatus.CREATED).build(); // 201
        } catch (IllegalArgumentException e) { // email duplicate
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}
