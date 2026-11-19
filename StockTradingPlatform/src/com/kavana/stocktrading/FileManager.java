package com.kavana.stocktrading;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FileManager {
    private final Path dataDirectory = Paths.get("data");
    private final Path accountFile = dataDirectory.resolve("account.txt");
    private final Path holdingsFile = dataDirectory.resolve("holdings.csv");
    private final Path transactionsFile = dataDirectory.resolve("transactions.csv");

    public static class LoadedData {
        public User user;
        public Portfolio portfolio;
        public List<Transaction> transactions;
    }

    public LoadedData load() throws IOException {
        Files.createDirectories(dataDirectory);
        LoadedData data = new LoadedData();
        String name = "Trader";
        double balance = 50000.00;

        if (Files.exists(accountFile)) {
            List<String> lines = Files.readAllLines(accountFile);
            if (lines.size() >= 2) {
                name = lines.get(0);
                try { balance = Double.parseDouble(lines.get(1)); }
                catch (NumberFormatException ignored) { balance = 50000.00; }
            }
        }

        data.user = new User(name, balance);
        data.portfolio = new Portfolio();
        data.transactions = new ArrayList<>();

        if (Files.exists(holdingsFile)) {
            try (BufferedReader reader = Files.newBufferedReader(holdingsFile)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] p = line.split(",", -1);
                    if (p.length == 3) {
                        try {
                            data.portfolio.buy(p[0], Integer.parseInt(p[1]), Double.parseDouble(p[2]));
                        } catch (NumberFormatException ignored) { }
                    }
                }
            }
        }

        if (Files.exists(transactionsFile)) {
            try (BufferedReader reader = Files.newBufferedReader(transactionsFile)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] p = line.split(",", -1);
                    if (p.length == 6) {
                        try {
                            data.transactions.add(new Transaction(p[0], p[1], p[2],
                                    Integer.parseInt(p[3]), Double.parseDouble(p[4]),
                                    LocalDateTime.parse(p[5])));
                        } catch (RuntimeException ignored) { }
                    }
                }
            }
        }
        return data;
    }

    public void save(TradingService service) throws IOException {
        Files.createDirectories(dataDirectory);
        try (BufferedWriter writer = Files.newBufferedWriter(accountFile)) {
            writer.write(service.getUser().getName());
            writer.newLine();
            writer.write(Double.toString(service.getUser().getBalance()));
        }

        try (BufferedWriter writer = Files.newBufferedWriter(holdingsFile)) {
            for (Map.Entry<String, Portfolio.Holding> entry
                    : service.getPortfolio().getHoldings().entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue().getQuantity()
                        + "," + entry.getValue().getAverageBuyPrice());
                writer.newLine();
            }
        }

        try (BufferedWriter writer = Files.newBufferedWriter(transactionsFile)) {
            for (Transaction transaction : service.getTransactions()) {
                writer.write(transaction.toCsv());
                writer.newLine();
            }
        }
    }
}
