package com.kavana.stocktrading;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class StockTradingGUI extends JFrame {
    private static final long serialVersionUID = 1L;
    private final FileManager fileManager = new FileManager();
    private final Market market = new Market();
    private TradingService service;

    private final JLabel balanceLabel = new JLabel();
    private final JLabel valueLabel = new JLabel();
    private final JLabel profitLabel = new JLabel();
    private final DefaultTableModel marketModel = model("Symbol", "Company", "Price (INR)");
    private final DefaultTableModel portfolioModel =
            model("Symbol", "Company", "Quantity", "Average Buy", "Current Price", "Current Value", "P/L");
    private final DefaultTableModel historyModel =
            model("Date & Time", "Type", "Symbol", "Quantity", "Price", "Total");
    private final JComboBox<String> symbolBox = new JComboBox<>();
    private final JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100000, 1));

    private static DefaultTableModel model(String... columns) {
        return new DefaultTableModel(columns, 0) {
            private static final long serialVersionUID = 1L;
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
    }

    private String money(double value) {
        return NumberFormat.getCurrencyInstance(new Locale("en", "IN")).format(value);
    }

    public StockTradingGUI() {
        super("Stock Trading Platform");
        try {
            FileManager.LoadedData data = fileManager.load();
            service = new TradingService(data.user, data.portfolio, market, data.transactions);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Could not load saved data: " + ex.getMessage());
            service = new TradingService(new User("Trader", 50000), new Portfolio(),
                    market, new ArrayList<>());
        }

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1050, 680);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(12, 12));

        JLabel heading = new JLabel("STOCK TRADING PLATFORM", SwingConstants.CENTER);
        heading.setFont(new Font("SansSerif", Font.BOLD, 25));
        heading.setForeground(new Color(28, 65, 115));
        heading.setBorder(BorderFactory.createEmptyBorder(18, 10, 8, 10));
        add(heading, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Market", createMarketPanel());
        tabs.addTab("Buy / Sell", createTradePanel());
        tabs.addTab("My Portfolio", createPortfolioPanel());
        tabs.addTab("Transaction History", createHistoryPanel());
        add(tabs, BorderLayout.CENTER);

        JPanel footer = new JPanel(new GridLayout(1, 3, 10, 0));
        footer.setBorder(BorderFactory.createEmptyBorder(8, 14, 14, 14));
        balanceLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        valueLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        profitLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        footer.add(balanceLabel);
        footer.add(valueLabel);
        footer.add(profitLabel);
        add(footer, BorderLayout.SOUTH);

        refreshAll();
    }

    private JPanel createMarketPanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        panel.add(new JLabel("Available shares (sample simulation prices)"), BorderLayout.NORTH);
        panel.add(new JScrollPane(new JTable(marketModel)), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createTradePanel() {
        JPanel panel = new JPanel(new GridLayout(7, 1, 8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(45, 180, 45, 180));
        JLabel info = new JLabel("Choose a stock and enter the quantity", SwingConstants.CENTER);
        info.setFont(new Font("SansSerif", Font.BOLD, 17));
        for (Stock stock : market.getAllStocks()) symbolBox.addItem(stock.getSymbol());
        panel.add(info);
        panel.add(new JLabel("Stock symbol:"));
        panel.add(symbolBox);
        panel.add(new JLabel("Quantity:"));
        panel.add(quantitySpinner);

        JPanel buttons = new JPanel();
        JButton buyButton = new JButton("BUY");
        JButton sellButton = new JButton("SELL");
        buyButton.setBackground(new Color(210, 240, 218));
        sellButton.setBackground(new Color(250, 220, 220));
        buyButton.addActionListener(e -> performTrade(true));
        sellButton.addActionListener(e -> performTrade(false));
        buttons.add(buyButton);
        buttons.add(sellButton);
        panel.add(buttons);

        JButton saveButton = new JButton("Save Portfolio");
        saveButton.addActionListener(e -> saveData(true));
        JPanel savePanel = new JPanel();
        savePanel.add(saveButton);
        panel.add(savePanel);
        return panel;
    }

    private JPanel createPortfolioPanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        panel.add(new JLabel("Your current holdings and performance"), BorderLayout.NORTH);
        panel.add(new JScrollPane(new JTable(portfolioModel)), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        panel.add(new JLabel("Completed buy and sell transactions"), BorderLayout.NORTH);
        panel.add(new JScrollPane(new JTable(historyModel)), BorderLayout.CENTER);
        return panel;
    }

    private void performTrade(boolean isBuy) {
        String symbol = (String) symbolBox.getSelectedItem();
        int quantity = (Integer) quantitySpinner.getValue();
        try {
            if (isBuy) service.buy(symbol, quantity);
            else service.sell(symbol, quantity);
            refreshAll();
            saveData(false);
            JOptionPane.showMessageDialog(this,
                    (isBuy ? "Purchase" : "Sale") + " completed successfully!");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Trade not completed",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void refreshAll() {
        marketModel.setRowCount(0);
        for (Stock stock : market.getAllStocks()) {
            marketModel.addRow(new Object[] { stock.getSymbol(), stock.getName(), money(stock.getPrice()) });
        }

        portfolioModel.setRowCount(0);
        for (java.util.Map.Entry<String, Portfolio.Holding> entry
                : service.getPortfolio().getHoldings().entrySet()) {
            Stock stock = market.getStock(entry.getKey());
            Portfolio.Holding holding = entry.getValue();
            double current = holding.getQuantity() * stock.getPrice();
            double pl = current - holding.getQuantity() * holding.getAverageBuyPrice();
            portfolioModel.addRow(new Object[] { stock.getSymbol(), stock.getName(),
                    holding.getQuantity(), money(holding.getAverageBuyPrice()),
                    money(stock.getPrice()), money(current), money(pl) });
        }

        historyModel.setRowCount(0);
        for (Transaction t : service.getTransactions()) {
            historyModel.addRow(new Object[] { t.toDisplayString(), t.getType(), t.getSymbol(),
                    t.getQuantity(), money(t.getPrice()), money(t.getTotal()) });
        }

        balanceLabel.setText("Cash balance: " + money(service.getUser().getBalance()));
        valueLabel.setText("Portfolio value: " + money(service.getPortfolio().getCurrentValue(market)));
        double pl = service.getPortfolio().getProfitLoss(market);
        profitLabel.setText("Unrealized P/L: " + money(pl));
        profitLabel.setForeground(pl >= 0 ? new Color(0, 125, 55) : Color.RED);
    }

    private void saveData(boolean showMessage) {
        try {
            fileManager.save(service);
            if (showMessage) JOptionPane.showMessageDialog(this,
                    "Portfolio saved in the project's data folder.");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Could not save data: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StockTradingGUI().setVisible(true));
    }
}
