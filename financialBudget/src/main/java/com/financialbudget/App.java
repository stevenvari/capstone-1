package com.financialbudget;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class App {
    private static Scanner keystrokes = new Scanner(System.in);
    private static  ArrayList<Transaction> transactions = new ArrayList<>();
    public static void main(String[] args) {

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
        //keystrokes.close();

    }

    public static void mainMenu() {
        System.out.println("Welcome to the Financial Application");
        System.out.println("Home Screen:");
        System.out.println("D- Add Deposit");
        System.out.println("P- Make Payment (Debit)");
        System.out.println("L- Ledger");
        System.out.println("M- Go to main menu");
        System.out.println("X- Exit");
        System.out.print("Please choose an option: ");

    }

    public static void addDeposit() {
        System.out.println("Add Deposit");
        System.out.println("-------------");

        System.out.println("Enter description ");
        String description = keystrokes.nextLine();

        System.out.println("Enter the vendor ");
        String vendor = keystrokes.nextLine();

        System.out.println("Enter the price ");
        double price = keystrokes.nextDouble();
        keystrokes.nextLine();

        System.out.println("Press enter to save it");
        keystrokes.nextLine();

        Transaction transaction = new Transaction(LocalDateTime.now(), description, vendor, price);
        saveTransaction(transaction);
    }

    public static void saveTransaction(Transaction transaction) {
        try {
            FileWriter fileWriter = new FileWriter("src/data/transactions.csv", true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write("\n");
            bufferedWriter.write(transaction.toString());
            bufferedWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred saving the deposit. Please try again.");
            e.getStackTrace();
        }
    }

    private static ArrayList<Transaction> readTransaction() {
        ArrayList<Transaction> transactions = new ArrayList<>();

        try {

            FileReader fileReader = new FileReader("src/data/transactions.csv");
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            bufferedReader.readLine();

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] tokens = line.split("\\|");

                LocalDateTime dateTime = LocalDateTime.parse(tokens[0] + "T" + tokens[1]);
                String description = tokens[2];
                String vendor = tokens[3];
                double price = Double.parseDouble(tokens[4]);

                Transaction transaction = new Transaction(dateTime, description, vendor, price);
                transactions.add(transaction);
                System.out.println(transaction.display());

            }
            bufferedReader.close();

        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            //e.printStackTrace();
        } catch (
                NumberFormatException e) {
            System.out.println("Invalid number format in file.");
        }
        return transactions;
    }

    public static void makePayment() {
        System.out.println("Add Payment");
        System.out.println("-------------");

        System.out.println("Enter description ");
        String description = keystrokes.nextLine();

        System.out.println("Enter the vendor ");
        String vendor = keystrokes.nextLine();

        System.out.println("Enter the price ");
        double price = keystrokes.nextDouble();
        keystrokes.nextLine();

        System.out.println("Press enter to save it");
        keystrokes.nextLine();


        Transaction transaction = new Transaction(LocalDateTime.now(), description, vendor, price);
        saveTransaction(transaction);

    }

    public static void ledgerMenu() {


        boolean running = true;

        while (running) {
            System.out.println("Ledger screen");
            System.out.println("----------------");
            System.out.println("A - All transactions");
            System.out.println("D - View Deposits Only");
            System.out.println("P - View Payments Only");
            System.out.println("R - View Reports");
            System.out.println("M - main menu");
            System.out.println("Please choose an option: ");

            String selectOption = keystrokes.nextLine();

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
                case "X":
                    System.out.println("Goodbye");
                    running = false;
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
        System.out.println("Transactions ");
        System.out.println("----------------------");
        System.out.println("This are all");


        ArrayList<Transaction> transactions = readTransaction();
        displayTransaction(transactions);

        System.out.println("Press enter to continue");
        keystrokes.nextLine();
    }

    private static void displayTransaction(ArrayList<Transaction> transactions) {
        for (Transaction transaction : transactions) {
            System.out.println(transaction.display());
        }
    }

    public static void viewOnlyDeposits () {
           System.out.println("Deposits ");
           System.out.println("..........");

        boolean foundDeposits = false;

           for (Transaction transaction : transactions) {
            if (transaction.getPrice() > 0){
                System.out.println(transaction.display());
                foundDeposits = true;
            }
        }

       if(!foundDeposits){
           System.out.println("no deposits found.");
       }

        System.out.println("press enter to continue..");
        keystrokes.nextLine();


    }

    public static void viewOnlyPayments() {
        System.out.println("view only payments ");
        System.out.println("=================");


    }

    public static void viewReports() {
        System.out.println("Run reports ");
        System.out.println("--------------");
        System.out.println("What report do you want to run");

        System.out.println(" 1- Month to date ");
        System.out.println(" 2- Previous Month ");
        System.out.println(" 3- Year to date ");
        System.out.println(" 4- Previous year ");
        System.out.println(" 5- Search by vendor ");
        System.out.println(" 0 Exit");
        System.out.println("choose an option");


        boolean running = true;
        while (running) {

            int selectedMenuOption = keystrokes.nextInt();

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
        System.out.println("Transactions over the last month date ");
        System.out.println("...............................");

        //LocalDateTime firstOfTheMonth = LocalDateTime.now().withDayOfMonth(1);
        LocalDateTime currentDate = LocalDateTime.now();


        ArrayList<Transaction> transactions = readTransaction();

        ArrayList<Transaction> overTheLastMonthDate = new ArrayList<>();
        for (Transaction transaction : transactions) {

            if (transaction.getDateTime().getMonth() == currentDate.getMonth() && transaction.getDateTime().getYear() == currentDate.getYear()) {
                overTheLastMonthDate.add(transaction);
            }


        }

        displayTransaction(overTheLastMonthDate);

    }

    public static void previousToMonth() {
        LocalDateTime currentDateTime = LocalDateTime.now();

        ArrayList<Transaction>transactions = readTransaction();

        ArrayList<Transaction> previousMonth = new ArrayList<>();
        for (Transaction transaction : transactions){

            if (transaction.getDateTime().getMonth() == currentDateTime.getMonth() && transaction.getDateTime().getYear() == currentDateTime.getYear()){
                previousMonth.add(transaction);
            }
        }
        displayTransaction(previousMonth);
    }

    public static void yearToDate() {

        LocalDateTime today = LocalDateTime.now();
        ArrayList<Transaction>transactions = readTransaction();

        ArrayList<Transaction> yearToDate = new ArrayList<>();

        LocalDateTime startOfYear = today
                .with(ChronoField.DAY_OF_YEAR, 1)        // jump to Jan 1 of this year, same time of day
                .truncatedTo(ChronoUnit.DAYS);           // zero‐out the time component

        List<Transaction> filtered = new ArrayList<>();

        for (Transaction t : transactions) {
            LocalDateTime dateTime = t.getDateTime();   // now a LocalDateTime
            if (!dateTime.isBefore(startOfYear) && !dateTime.isAfter(today)) {
                filtered.add(t);
            }
        }

        displayTransaction(yearToDate);



    }

    public static void previousToYear() {

    }

    public static void searchTransactionByVendor() {



    }
}

