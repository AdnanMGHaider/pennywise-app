package com.pennywise.repository;

import com.pennywise.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    /**
     * Fetch all transactions belonging to a given user, sorted by date descending.
     */
    List<Transaction> findAllByUserIdOrderByDateDesc(UUID userId);
}
