package com.kavana.stocktrading;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {
    private final String type;
    private final String symbol;
    private final String stockName;
    private final int quantity;
    private final double price;
    private final LocalDateTime dateTime;

    public Transaction(String type, String symbol, String stockName, int quantity, double price) {
        this(type, symbol, stockName, quantity, price, LocalDateTime.now());
    }

    public Transaction(String type, String symbol, String stockName, int quantity,
                       double price, LocalDateTime dateTime) {
        this.type = type;
        this.symbol = symbol;
        this.stockName = stockName;
        this.quantity = quantity;
        this.price = price;
        this.dateTime = dateTime;
    }

    public String getType() { return type; }
    public String getSymbol() { return symbol; }
    public String getStockName() { return stockName; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }
    public LocalDateTime getDateTime() { return dateTime; }
    public double getTotal() { return quantity * price; }

    public String toCsv() {
        return type + "," + symbol + "," + stockName.replace(",", " ") + ","
                + quantity + "," + price + "," + dateTime;
    }

    public String toDisplayString() {
        return dateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
    }
}
