package com.kavana.stocktrading;

public class Stock {
    private final String symbol;
    private final String name;
    private double price;

    public Stock(String symbol, String name, double price) {
        this.symbol = symbol;
        this.name = name;
        this.price = price;
    }

    public String getSymbol() { return symbol; }
    public String getName() { return name; }
    public double getPrice() { return price; }

    public void setPrice(double price) {
        if (price <= 0) throw new IllegalArgumentException("Stock price must be positive.");
        this.price = price;
    }
}
