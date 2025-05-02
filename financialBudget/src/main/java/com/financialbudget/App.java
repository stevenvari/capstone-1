package com.financialbudget;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {
    private static Scanner keystrokes = new Scanner(System.in);
    public static ArrayList<Transaction> transactions = new ArrayList<>();
    private static final String DATA_FILE = "src/data/transactions.csv";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final DateTimeFormatter DATE_TIME_FORMATTER_FOR_READING = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public static void main(String[] args) {
        loadTransactions(); // Load transactions at the start

        boolean running = true;
        while (running) {
            mainMenu();
            String choice = keystrokes.nextLine().trim().toUpperCase();

            switch (choice) {
                case "D":
                    addDeposit();
                    break;
                case "P":
                    makePayment();
                    break;
                case "L":
                    ledgerMenu();
                    break;
                case "X":
                    System.out.println("Goodbye");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid menu option. Try again.");
            }
        }
        keystrokes.close(); // Close the scanner when the application exits
    }

    public static void mainMenu() {
        System.out.println("\nWelcome to the Financial Application");
        System.out.println("Home Screen:");
        System.out.println("D- Add Deposit");
        System.out.println("P- Make Payment (Debit)");
        System.out.println("L- Ledger");
        System.out.println("X- Exit");
        System.out.print("Please choose an option: ");
    }

    public static void addDeposit() {
        System.out.println("\nAdd Deposit");
        System.out.println("-------------");

        System.out.print("Enter description: ");
        String description = keystrokes.nextLine();

        System.out.print("Enter the vendor: ");
        String vendor = keystrokes.nextLine();

        System.out.print("Enter the amount: ");
        double price = keystrokes.nextDouble();
        keystrokes.nextLine(); // Consume newline

        Transaction transaction = new Transaction(LocalDateTime.now(), description, vendor, price);
        saveTransaction(transaction);
        System.out.println("Deposit saved.");
    }

    public static void saveTransaction(Transaction transaction) {
        try (FileWriter fileWriter = new FileWriter(DATA_FILE, true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            String dateString = transaction.getDateTime().toLocalDate().format(DATE_FORMATTER);
            String timeString = transaction.getDateTime().toLocalTime().format(TIME_FORMATTER);

            bufferedWriter.write(String.join("|",
                    dateString,
                    timeString,
                    transaction.getDescription(),
                    transaction.getVendor(),
                    String.valueOf(transaction.getPrice())));
            bufferedWriter.newLine(); // Use newLine() for proper line breaks
        } catch (IOException e) {
            System.out.println("An error occurred saving the transaction: " + e.getMessage());
        }
    }

    private static void loadTransactions() {
        transactions.clear(); // Clear existing transactions before loading
        try (FileReader fileReader = new FileReader(DATA_FILE);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            String headerLine = bufferedReader.readLine(); // Read and discard the header line
            if (headerLine != null && headerLine.contains("data|time")) {
                System.out.println("Skipping header row: " + headerLine);
            }

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] tokens = line.split("\\|");
                if (tokens.length == 5) {
                    try {
                        LocalDateTime dateTime = LocalDateTime.parse(tokens[0] + "T" + tokens[1], DATE_TIME_FORMATTER_FOR_READING);
                        String description = tokens[2];
                        String vendor = tokens[3];
                        double price = Double.parseDouble(tokens[4]);

                        Transaction transaction = new Transaction(dateTime, description, vendor, price);
                        transactions.add(transaction);
                    } catch (DateTimeParseException e) {
                        System.out.println("Error parsing date/time in line: " + line + " - Expected format:yyyy-MM-dd|HH:mm:ss - " + e.getMessage());
                    } catch (NumberFormatException e) {
                        System.out.println("Error parsing price in line: " + line + " - Expected a number - " + e.getMessage());
                    }
                } else if (!line.trim().isEmpty()) { // Ignore empty lines
                    System.out.println("Skipping invalid line: " + line + " - Expected 5 tokens, but found " + tokens.length);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading transactions: " + e.getMessage());
        }
    }


    public static void makePayment() {
        System.out.println("\nMake Payment (Debit)");
        System.out.println("==================");

        System.out.print("Enter description: ");
        String description = keystrokes.nextLine();

        System.out.print("Enter the vendor: ");
        String vendor = keystrokes.nextLine();

        System.out.print("Enter the amount: ");
        double price = keystrokes.nextDouble();
        keystrokes.nextLine(); // Consume newline

        Transaction transaction = new Transaction(LocalDateTime.now(), description, vendor, -price); // Debit is negative
        saveTransaction(transaction);
        System.out.println("Payment saved.");
    }

    public static void ledgerMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\nLedger Screen");
            System.out.println("----------------");
            System.out.println("A - All transactions");
            System.out.println("D - View Deposits Only");
            System.out.println("P - View Payments Only");
            System.out.println("R - View Reports");
            System.out.println("M - Main Menu");
            System.out.print("Please choose an option: ");

            String selectOption = keystrokes.nextLine().trim().toUpperCase();

            switch (selectOption) {
                case "A":
                    viewAllTransactions();
                    break;
                case "D":
                    viewOnlyDeposits();
                    break;
                case "P":
                    viewOnlyPayments();
                    break;
                case "R":
                    viewReports();
                    break;
                case "M":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid menu option. Try again.");
            }
        }
    }

    public static void viewAllTransactions() {
        System.out.println("\n--- All Transactions ---");
        if (transactions.isEmpty()) {
            System.out.println("No transactions recorded yet.");
        } else {
            displayTransactions(transactions);
        }
        System.out.print("Press enter to continue...");
        keystrokes.nextLine();
    }

    private static void displayTransactions(List<Transaction> transactionList) {
        DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (Transaction transaction : transactionList) {
            System.out.println("Date/Time: " + transaction.getDateTime().format(displayFormatter));
            System.out.println("Description: " + transaction.getDescription());
            System.out.println("Vendor: " + transaction.getVendor());
            System.out.println("Price: $" + String.format("%.2f", transaction.getPrice()));
            System.out.println("----------------------");
        }
    }

    public static void viewOnlyDeposits() {
        System.out.println("\n--- Deposits Only ---");
        List<Transaction> deposits = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (transaction.getPrice() > 0) {
                deposits.add(transaction);
            }
        }
        if (deposits.isEmpty()) {
            System.out.println("No deposits recorded.");
        } else {
            displayTransactions(deposits);
        }
        System.out.print("Press enter to continue...");
        keystrokes.nextLine();
    }

    public static void viewOnlyPayments() {
        System.out.println("\n--- Payments Only ---");
        List<Transaction> payments = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (transaction.getPrice() < 0) {
                payments.add(transaction);
            }
        }
        if (payments.isEmpty()) {
            System.out.println("No payments recorded.");
        } else {
            displayTransactions(payments);
        }
        System.out.print("Press enter to continue...");
        keystrokes.nextLine();
    }

    public static void viewReports() {
        boolean running = true;
        while (running) {
            System.out.println("\n--- Run Reports ---");
            System.out.println("1 - Month to date");
            System.out.println("2 - Previous Month");
            System.out.println("3 - Year to date");
            System.out.println("4 - Previous year");
            System.out.println("5 - Search by vendor");
            System.out.println("0 - Back to Ledger Menu");
            System.out.print("Choose an option: ");

            int selectedMenuOption = keystrokes.nextInt();
            keystrokes.nextLine(); // Consume newline

            switch (selectedMenuOption) {
                case 1:
                    MonthToDate();
                    break;
                case 2:
                    previousToMonth();
                    break;
                case 3:
                    yearToDate();
                    break;
                case 4:
                    previousToYear();
                    break;
                case 5:
                    searchTransactionByVendor();
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid report menu option. Try again.");
            }
        }
    }

    public static void MonthToDate() {
        System.out.println("\n--- Transactions This Month ---");
        LocalDateTime now = LocalDateTime.now();
        List<Transaction> currentMonthTransactions = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (transaction.getDateTime().getMonth() == now.getMonth() && transaction.getDateTime().getYear() == now.getYear()) {
                currentMonthTransactions.add(transaction);
            }
        }
        displayTransactions(currentMonthTransactions);
        System.out.print("Press enter to continue...");
        keystrokes.nextLine();
    }

    public static void previousToMonth() {
        System.out.println("\n--- Transactions Last Month ---");
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime previousMonth = now.minusMonths(1);
        List<Transaction> lastMonthTransactions = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (transaction.getDateTime().getMonth() == previousMonth.getMonth() && transaction.getDateTime().getYear() == previousMonth.getYear()) {
                lastMonthTransactions.add(transaction);
            }
        }
        displayTransactions(lastMonthTransactions);
        System.out.print("Press enter to continue...");
        keystrokes.nextLine();
    }

    public static void yearToDate() {
        System.out.println("\n--- Transactions This Year ---");
        LocalDateTime now = LocalDateTime.now();
        List<Transaction> currentYearTransactions = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (transaction.getDateTime().getYear() == now.getYear()) {
                currentYearTransactions.add(transaction);
            }
        }
        displayTransactions(currentYearTransactions);
        System.out.print("Press enter to continue...");
        keystrokes.nextLine();
    }

    public static void previousToYear() {
        System.out.println("\n--- Transactions Last Year ---");
        LocalDateTime now = LocalDateTime.now();
        List<Transaction> lastYearTransactions = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (transaction.getDateTime().getYear() == now.minusYears(1).getYear()) {
                lastYearTransactions.add(transaction);
            }
        }
        displayTransactions(lastYearTransactions);
        System.out.print("Press enter to continue...");
        keystrokes.nextLine();
    }

    public static void searchTransactionByVendor() {
        System.out.println("\n--- Search Transactions by Vendor ---");
        System.out.print("Enter the vendor to search for: ");
        String vendorToSearch = keystrokes.nextLine().trim();
        List<Transaction> matchingTransactions = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (transaction.getVendor().equalsIgnoreCase(vendorToSearch)) {
                matchingTransactions.add(transaction);
            }
        }
        if (matchingTransactions.isEmpty()) {
            System.out.println("No transactions found for vendor: " + vendorToSearch);
        } else {
            displayTransactions(matchingTransactions);
        }
        System.out.print("Press enter to continue...");
        keystrokes.nextLine();
    }
}