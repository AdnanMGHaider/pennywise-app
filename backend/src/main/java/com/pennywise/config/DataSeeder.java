// package com.pennywise.config;

// import com.pennywise.model.Transaction;
// import com.pennywise.model.User;
// import com.pennywise.repository.UserRepository;
// import com.pennywise.service.TransactionService;
// import org.springframework.boot.CommandLineRunner;
// import org.springframework.stereotype.Component;

// import java.math.BigDecimal;
// import java.math.RoundingMode;
// import java.time.Instant;
// import java.time.temporal.ChronoUnit;
// import java.util.*;

// /**
// * Seeds the database with mock transactions for each existing user.
// * Runs once at application startup.
// */
// @Component
// public class DataSeeder implements CommandLineRunner {

// private final UserRepository userRepository;
// private final TransactionService transactionService;

// public DataSeeder(UserRepository userRepository,
// TransactionService transactionService) {
// this.userRepository = userRepository;
// this.transactionService = transactionService;
// }

// @Override
// public void run(String... args) throws Exception {
// // List of sample categories and merchants
// List<String> categories = Arrays.asList("Food", "Utilities", "Entertainment",
// "Travel", "Groceries", "Health",
// "Rent");
// List<String> merchants = Arrays.asList(
// "Cafe Delight", "UtilityCo", "Movie Plex", "Fly High Airways", "SuperMart",
// "PharmaCare",
// "HomeRentals Inc.");

// // For each user in the database, generate 20,000 transactions
// List<User> users = userRepository.findAll();
// Random random = new Random();

// for (User user : users) {
// List<Transaction> batch = new ArrayList<>(5000);
// Instant now = Instant.now();
// for (int i = 0; i < 20_000; i++) {
// // Random date within the past 365 days
// long daysAgo = random.nextInt(365);
// long secondsOffset = random.nextInt(86_400); // seconds in a day
// Instant date = now.minus(daysAgo, ChronoUnit.DAYS).minus(secondsOffset,
// ChronoUnit.SECONDS);

// // Random amount between 1.00 and 500.00, rounded to two decimals
// double rawAmount = 1.0 + (500.0 - 1.0) * random.nextDouble();
// BigDecimal amount = BigDecimal.valueOf(rawAmount)
// .setScale(2, RoundingMode.HALF_UP);

// // Random category and merchant
// String category = categories.get(random.nextInt(categories.size()));
// String merchant = merchants.get(random.nextInt(merchants.size()));

// // 50% chance to include latitude/longitude
// Double lat = null;
// Double lng = null;
// if (random.nextBoolean()) {
// // Example bounds: lat ~ [43.0, 44.0], lng ~ [-80.0, -79.0]
// lat = 43.0 + random.nextDouble();
// lng = -80.0 + random.nextDouble();
// }

// // Optional description
// String description = "Auto-generated transaction";

// // Create the Transaction entity
// Transaction tx = new Transaction(
// user.getId(),
// date,
// amount,
// category,
// merchant,
// lat,
// lng,
// description);
// batch.add(tx);

// // To avoid an extremely large in-memory list, flush every 5,000 items
// if (batch.size() == 5_000) {
// for (Transaction t : batch) {
// transactionService.createTransaction(t);
// }
// batch.clear();
// }
// }
// // Save any remaining transactions
// for (Transaction t : batch) {
// transactionService.createTransaction(t);
// }
// batch.clear();
// System.out.println("Seeded 20,000 transactions for user: " +
// user.getEmail());
// }
// }
// }
