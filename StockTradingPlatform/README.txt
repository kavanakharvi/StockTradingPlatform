STOCK TRADING PLATFORM - JAVA SWING PROJECT

ABOUT
This project simulates a small stock trading environment. It uses Java OOP,
Swing GUI, ArrayList/Map collections, and file handling.

IMPORTANT
- Stock prices are sample values for academic simulation only.
- This application does not connect to a real stock exchange.
- It does not place real trades or provide investment advice.
- The starting cash balance for a new user is INR 50,000.

FEATURES
1. Market tab: view sample stock symbols, names and prices.
2. Buy / Sell tab: buy shares or sell shares already owned.
3. My Portfolio tab: view quantity, average purchase price, current value and P/L.
4. Transaction History tab: review completed trades.
5. File handling: automatically saves after successful trades.
   You can also press Save Portfolio.

ECLIPSE IMPORT STEPS
1. Download and extract StockTradingPlatform.zip.
2. Open Eclipse.
3. Select File > Import.
4. Select General > Existing Projects into Workspace, then click Next.
5. Browse to the extracted StockTradingPlatform folder.
6. Select the project and click Finish.
7. Expand src > com.kavana.stocktrading.
8. Right-click StockTradingGUI.java > Run As > Java Application.

RUNNING WITHOUT IMPORT
If you create a Java project manually, create package:
com.kavana.stocktrading
Then copy all Java files from the matching package folder.

DATA STORAGE
When the program runs, it creates a "data" folder in the Eclipse project's
working directory. It stores:
- account.txt
- holdings.csv
- transactions.csv

To reset the simulation, close the application and delete those three files.
The next run starts with a fresh account and INR 50,000.

OOP CONCEPTS
- Encapsulation: private fields with public methods.
- Classes and objects: Stock, User, Portfolio, Transaction, Market.
- Abstraction: TradingService provides buy and sell operations.
- Collections: Map stores holdings; List stores transaction history.

PORTFOLIO CALCULATIONS
Current value = quantity x current sample stock price.
Invested value = quantity x average buy price.
Unrealized profit/loss = current value - invested value.

NOTE
Prices are fixed sample values in this basic version. Therefore, profit/loss
changes when the user buys or sells at different sample prices, but there is
no automatic live price feed or time-series market chart.
