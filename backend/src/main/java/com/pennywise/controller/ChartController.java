package com.pennywise.controller;

import com.pennywise.controller.CategorySummary;
import com.pennywise.controller.DailySummary;
import com.pennywise.model.User;
import com.pennywise.service.ChartService;
import com.pennywise.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/charts")
public class ChartController {

    private final ChartService chartService;
    private final UserService userService;

    @Autowired
    public ChartController(ChartService chartService, UserService userService) {
        this.chartService = chartService;
        this.userService = userService;
    }

    // Part 1: Category breakdown
    @GetMapping("/categories")
    public ResponseEntity<List<CategorySummary>> getCategoryBreakdown(
            @AuthenticationPrincipal UserDetails userDetails) {

        UUID userId = userService.findByEmail(userDetails.getUsername())
                .map(User::getId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        List<CategorySummary> data = chartService.getCategoryBreakdown(userId, 30);
        return ResponseEntity.ok(data);
    }

    // Part 2: Daily totals
    @GetMapping("/daily")
    public ResponseEntity<List<DailySummary>> getDailySpending(
            @AuthenticationPrincipal UserDetails userDetails) {

        UUID userId = userService.findByEmail(userDetails.getUsername())
                .map(User::getId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        List<DailySummary> data = chartService.getDailySpending(userId, 30);
        return ResponseEntity.ok(data);
    }
}
