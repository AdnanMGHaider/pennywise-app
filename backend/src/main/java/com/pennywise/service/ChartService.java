package com.pennywise.service;

import com.pennywise.controller.CategorySummary;
import com.pennywise.controller.DailySummary;
import com.pennywise.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ChartService {

    private final TransactionRepository txRepo;
    private final UserService userService;

    @Autowired
    public ChartService(TransactionRepository txRepo, UserService userService) {
        this.txRepo = txRepo;
        this.userService = userService;
    }

    // Category breakdown unchanged...
    public List<CategorySummary> getCategoryBreakdown(UUID userId, int days) {
        Instant from = Instant.now().minus(days, ChronoUnit.DAYS);
        return txRepo.findCategorySummaries(userId, from);
    }

    // Updated daily‐spending method:
    public List<DailySummary> getDailySpending(UUID userId, int days) {
        Instant from = Instant.now().minus(days, ChronoUnit.DAYS);

        // 1) Get raw rows
        List<Object[]> rows = txRepo.findDailySummariesRaw(userId, from);

        // 2) Map each row into DailySummary(date, amount)
        return rows.stream()
                .map(r -> {
                    Date sqlDate = (Date) r[0]; // java.sql.Date
                    LocalDate day = sqlDate.toLocalDate();
                    BigDecimal total = (BigDecimal) r[1];
                    return new DailySummary(day, total);
                })
                .collect(Collectors.toList());
    }
}
