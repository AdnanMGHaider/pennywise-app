package com.pennywise.repository;

import com.pennywise.controller.CategorySummary;
import com.pennywise.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

  List<Transaction> findAllByUserIdOrderByDateDesc(UUID userId);

  // — Category summaries, unchanged —
  @Query("""
        SELECT new com.pennywise.controller.CategorySummary(
          t.category, SUM(t.amount)
        )
        FROM Transaction t
        WHERE t.userId = :userId
          AND t.date >= :from
        GROUP BY t.category
      """)
  List<CategorySummary> findCategorySummaries(
      @Param("userId") UUID userId,
      @Param("from") Instant from);

  // — Change this: return raw Object[] rows —
  @Query(value = """
      SELECT
        DATE(t.date) AS day,
        SUM(t.amount) AS total
      FROM transactions t
      WHERE t.user_id = :userId
        AND t.date >= :from
      GROUP BY DATE(t.date)
      ORDER BY DATE(t.date)
      """, nativeQuery = true)
  List<Object[]> findDailySummariesRaw(
      @Param("userId") UUID userId,
      @Param("from") Instant from);
}
