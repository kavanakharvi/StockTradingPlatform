package com.kavana.stocktrading;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TradingService {
    private final User user;
    private final Portfolio portfolio;
    private final Market market;
    private final List<Transaction> transactions;

    public TradingService(User user, Portfolio portfolio, Market market,
                          List<Transaction> transactions) {
        this.user = user;
        this.portfolio = portfolio;
        this.market = market;
        this.transactions = transactions;
    }

    public void buy(String symbol, int quantity) {
        validateQuantity(quantity);
        Stock stock = requireStock(symbol);
        double total = stock.getPrice() * quantity;
        user.deductMoney(total);
        portfolio.buy(stock.getSymbol(), quantity, stock.getPrice());
        transactions.add(new Transaction("BUY", stock.getSymbol(), stock.getName(),
                quantity, stock.getPrice()));
    }

    public void sell(String symbol, int quantity) {
        validateQuantity(quantity);
        Stock stock = requireStock(symbol);
        if (quantity > portfolio.getQuantity(stock.getSymbol())) {
            throw new IllegalArgumentException("You do not own enough shares to sell.");
        }
        portfolio.sell(stock.getSymbol(), quantity);
        user.addMoney(stock.getPrice() * quantity);
        transactions.add(new Transaction("SELL", stock.getSymbol(), stock.getName(),
                quantity, stock.getPrice()));
    }

    private Stock requireStock(String symbol) {
        Stock stock = market.getStock(symbol);
        if (stock == null) throw new IllegalArgumentException("Stock symbol not found.");
        return stock;
    }

    private void validateQuantity(int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be at least 1.");
    }

    public User getUser() { return user; }
    public Portfolio getPortfolio() { return portfolio; }
    public Market getMarket() { return market; }
    public List<Transaction> getTransactions() { return Collections.unmodifiableList(transactions); }
}
