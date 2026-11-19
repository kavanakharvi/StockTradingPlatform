package com.kavana.stocktrading;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class Market {
    private final Map<String, Stock> stocks = new LinkedHashMap<>();

    public Market() {
        // Sample prices for simulation only; these are not live market prices.
        addStock(new Stock("TATA", "Tata Motors", 800.00));
        addStock(new Stock("INFY", "Infosys", 1500.00));
        addStock(new Stock("RELI", "Reliance Industries", 2900.00));
        addStock(new Stock("WIPRO", "Wipro", 450.00));
        addStock(new Stock("HDFCBANK", "HDFC Bank", 1650.00));
        addStock(new Stock("ITC", "ITC Limited", 430.00));
    }

    private void addStock(Stock stock) {
        stocks.put(stock.getSymbol(), stock);
    }

    public Collection<Stock> getAllStocks() { return stocks.values(); }

    public Stock getStock(String symbol) {
        if (symbol == null) return null;
        return stocks.get(symbol.trim().toUpperCase());
    }
}
