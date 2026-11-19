package com.kavana.stocktrading;

import java.util.LinkedHashMap;
import java.util.Map;

public class Portfolio {
    public static class Holding {
        private int quantity;
        private double averageBuyPrice;

        public Holding(int quantity, double averageBuyPrice) {
            this.quantity = quantity;
            this.averageBuyPrice = averageBuyPrice;
        }

        public int getQuantity() { return quantity; }
        public double getAverageBuyPrice() { return averageBuyPrice; }

        public void buy(int additionalQuantity, double price) {
            double totalCost = quantity * averageBuyPrice + additionalQuantity * price;
            quantity += additionalQuantity;
            averageBuyPrice = totalCost / quantity;
        }

        public void sell(int soldQuantity) {
            quantity -= soldQuantity;
        }
    }

    private final Map<String, Holding> holdings = new LinkedHashMap<>();

    public Map<String, Holding> getHoldings() { return holdings; }

    public int getQuantity(String symbol) {
        Holding holding = holdings.get(symbol);
        return holding == null ? 0 : holding.getQuantity();
    }

    public void buy(String symbol, int quantity, double price) {
        Holding holding = holdings.get(symbol);
        if (holding == null) holdings.put(symbol, new Holding(quantity, price));
        else holding.buy(quantity, price);
    }

    public void sell(String symbol, int quantity) {
        Holding holding = holdings.get(symbol);
        if (holding == null || quantity <= 0 || quantity > holding.getQuantity()) {
            throw new IllegalArgumentException("You do not own enough shares to sell.");
        }
        holding.sell(quantity);
        if (holding.getQuantity() == 0) holdings.remove(symbol);
    }

    public double getCurrentValue(Market market) {
        double total = 0;
        for (Map.Entry<String, Holding> entry : holdings.entrySet()) {
            Stock stock = market.getStock(entry.getKey());
            if (stock != null) total += entry.getValue().getQuantity() * stock.getPrice();
        }
        return total;
    }

    public double getInvestedValue() {
        double total = 0;
        for (Holding holding : holdings.values()) {
            total += holding.getQuantity() * holding.getAverageBuyPrice();
        }
        return total;
    }

    public double getProfitLoss(Market market) {
        return getCurrentValue(market) - getInvestedValue();
    }
}
