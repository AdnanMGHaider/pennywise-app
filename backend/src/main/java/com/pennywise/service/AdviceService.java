package com.pennywise.service;

import com.openai.client.OpenAIClient;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import com.pennywise.controller.CategorySummary;
import com.pennywise.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Generates money-saving advice via GPT-4o based on a user’s last 30 days of
 * spending.
 */
@Service
public class AdviceService {

    private final OpenAIClient openAIClient;
    private final UserService userService;
    private final TransactionService transactionService;

    @Autowired
    public AdviceService(OpenAIClient openAIClient,
            UserService userService,
            TransactionService transactionService) {
        this.openAIClient = openAIClient;
        this.userService = userService;
        this.transactionService = transactionService;
    }

    /** Public entry-point called by the controller. */
    public String generateAdvice(String userEmail) {
        // 1) Resolve email → userId
        UUID userId = userService.findByEmail(userEmail)
                .map(u -> u.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userEmail));

        // 2) Pull the user’s transactions for the last 30 days
        Instant from = Instant.now().minus(30, ChronoUnit.DAYS);
        List<Transaction> txs = transactionService.getTransactionsForUser(userId).stream()
                .filter(tx -> tx.getDate().isAfter(from))
                .collect(Collectors.toList());

        // 3a) Total spend
        BigDecimal totalSpend = txs.stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 3b) Top-3 categories
        Map<String, BigDecimal> byCategory = txs.stream()
                .collect(Collectors.groupingBy(
                        Transaction::getCategory,
                        Collectors.reducing(BigDecimal.ZERO,
                                Transaction::getAmount,
                                BigDecimal::add)));

        List<CategorySummary> topCats = byCategory.entrySet().stream()
                .map(e -> new CategorySummary(e.getKey(), e.getValue()))
                .sorted(Comparator.comparing(CategorySummary::getTotalAmount).reversed())
                .limit(3)
                .collect(Collectors.toList());

        // 4) Build prompt messages
        List<String> messages = new ArrayList<>();
        messages.add(
                "You are a personal-finance assistant. Provide 2-3 actionable money-saving tips based on the following summary:");
        messages.add(String.format("In the last 30 days, the user spent a total of $%.2f.", totalSpend));
        messages.add("Top spending categories:");
        topCats.forEach(cs -> messages.add(String.format("- %s: $%.2f", cs.getCategory(), cs.getTotalAmount())));
        messages.add("Give 2–3 concise, practical tips on how they could save.");

        // 5) Ask GPT-4o and return its reply
        return callChatCompletion(messages);
    }

    /** Wraps the OpenAI Chat Completions call. */
    private String callChatCompletion(List<String> userMessages) {
        ChatCompletionCreateParams.Builder builder = ChatCompletionCreateParams.builder()
                .model(ChatModel.GPT_4O_MINI)
                .store(true);

        userMessages.forEach(builder::addUserMessage);
        ChatCompletionCreateParams params = builder.build();

        ChatCompletion completion = openAIClient
                .chat()
                .completions()
                .create(params);

        // content() is Optional<String>; unwrap or fall back to empty string
        return completion.choices()
                .get(0)
                .message()
                .content()
                .orElse("")
                .strip();
    }
}
