package com.pennywise.controller;

import com.pennywise.service.AdviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/advice")
public class AdviceController {

    private final AdviceService adviceService;

    @Autowired
    public AdviceController(AdviceService adviceService) {
        this.adviceService = adviceService;
    }

    /**
     * GET /api/advice
     * Returns 2–3 money-saving tips for the authenticated user.
     */
    @GetMapping
    public ResponseEntity<Map<String, String>> getAdvice(
            @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        String advice = adviceService.generateAdvice(email);
        // Wrap in a JSON object: { "advice": "..." }
        return ResponseEntity.ok(Collections.singletonMap("advice", advice));
    }
}
