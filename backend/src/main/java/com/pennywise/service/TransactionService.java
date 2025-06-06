package com.pennywise.service;

import com.pennywise.model.Transaction;
import com.pennywise.model.User;
import com.pennywise.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserService userService; // added to resolve email → userId

    @Autowired
    public TransactionService(TransactionRepository transactionRepository,
            UserService userService) {
        this.transactionRepository = transactionRepository;
        this.userService = userService;
    }

    /**
     * Fetch all transactions for a given user, sorted by date descending.
     *
     * @param userId the UUID of the user
     * @return a list of transactions belonging to that user
     */
    public List<Transaction> getTransactionsForUser(UUID userId) {
        return transactionRepository.findAllByUserIdOrderByDateDesc(userId);
    }

    /**
     * Save a new transaction for a user.
     *
     * @param transaction a Transaction entity (with userId already set)
     * @return the saved Transaction (with generated ID)
     */
    public Transaction createTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    /**
     * Helper: Given an email (username), fetch the User and return its UUID.
     * Throws an exception if the user is not found (shouldn’t happen if JWT is
     * valid).
     *
     * @param email the user’s email
     * @return the UUID of that user
     */
    public UUID getUserIdByEmail(String email) {
        Optional<User> userOpt = userService.findByEmail(email);
        return userOpt
                .map(User::getId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + email));
    }
}
