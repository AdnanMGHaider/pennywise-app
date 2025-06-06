package com.pennywise.controller;

import com.pennywise.model.Transaction;
import com.pennywise.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Exposes endpoints under /api/transactions for fetching and creating
 * transactions.
 * These endpoints are protected by JWT (configured in SecurityConfig).
 */
@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * GET /api/transactions
     * Return all transactions for the authenticated user.
     *
     * @param userDetails injected by Spring via JWT filter (username is email)
     * @return list of TransactionResponse in JSON
     */
    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getAllTransactions(
            @AuthenticationPrincipal UserDetails userDetails) {
        // We stored email in the token’s subject. Now we need to convert email →
        // userId.
        // Ideally UserService or a dedicated Auth helper would provide this.
        // For now, assume UserService has a method findByEmail(...).
        UUID userId = transactionService
                .getUserIdByEmail(userDetails.getUsername());

        List<Transaction> transactions = transactionService.getTransactionsForUser(userId);

        List<TransactionResponse> response = transactions.stream()
                .map(tx -> new TransactionResponse(
                        tx.getId(),
                        tx.getDate(),
                        tx.getAmount(),
                        tx.getCategory(),
                        tx.getMerchant(),
                        tx.getLat(),
                        tx.getLng(),
                        tx.getDescription()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/transactions
     * Create a new transaction for the authenticated user.
     * Body: TransactionRequest
     */
    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody TransactionRequest request) {
        // Convert email → userId
        UUID userId = transactionService
                .getUserIdByEmail(userDetails.getUsername());

        Transaction txEntity = new Transaction(
                userId,
                request.getDate(),
                request.getAmount(),
                request.getCategory(),
                request.getMerchant(),
                request.getLat(),
                request.getLng(),
                request.getDescription());

        Transaction saved = transactionService.createTransaction(txEntity);

        TransactionResponse resp = new TransactionResponse(
                saved.getId(),
                saved.getDate(),
                saved.getAmount(),
                saved.getCategory(),
                saved.getMerchant(),
                saved.getLat(),
                saved.getLng(),
                saved.getDescription());

        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }
}
