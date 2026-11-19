package com.kavana.stocktrading;

public class User {
    private final String name;
    private double balance;

    public User(String name, double balance) {
        this.name = name;
        this.balance = balance;
    }

    public String getName() { return name; }
    public double getBalance() { return balance; }

    public void addMoney(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be positive.");
        balance += amount;
    }

    public void deductMoney(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be positive.");
        if (amount > balance) throw new IllegalArgumentException("Insufficient balance.");
        balance -= amount;
    }
}
