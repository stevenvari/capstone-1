package com.financialbudget;

import java.io.*;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.Scanner;


public class App {
     private static Scanner scanner = new Scanner(System.in);

     public static void main(String[] args) {

        boolean running = true;
        while (running){
            showMainMenu();
            String choice = scanner.nextLine().trim().toUpperCase();

            switch (choice){
                case "D":
                    addDeposit();
                    break;
                case "A":
                    viewAllTransactions();
                    break;

                case "P":
                    makePayment();
                    break;
                case "L":
                    ledger();
                    break;
                case "M":
                    backToMainMenu();
                    break;
                case "X":
                    System.out.println("Goodbye");
                    running =false;
                     break;

                default:
                    System.out.println("Invalid menu option. Try again.");
            }
        }
        scanner.close();

    }

    public static void showMainMenu(){
        System.out.println("Welcome to the Financial Application");
        System.out.println("Home Screen:");
        System.out.println("D- Add Deposit");
        System.out.println("P- Make Payment (Debit)");
        System.out.println("L- Ledger");
        System.out.println("A- All transactions");
        System.out.println("M- Go to main menu");
        System.out.println("X- Exit");
        System.out.print("Please choose an option: ");
    }
    public static void addDeposit(){
         System.out.println("Add Deposit");
         System.out.println("-------------");

         System.out.println("Enter description ");
         String description = scanner.nextLine();

         System.out.println("Enter the vendor ");
         String vendor = scanner.nextLine();

         System.out.println("Enter the price ");
         double price = scanner.nextDouble();

         System.out.println("Press enter to save it");
         scanner.nextLine();

         Transaction transaction = new Transaction(LocalDateTime.now(),description,vendor,price);
         saveDeposit(transaction);
         backToMainMenu();
    }
    public static  void saveDeposit(Transaction transaction ){
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
    public static void viewAllTransactions(){
         System.out.println("Wha would you like to do");
         System.out.println("----------------------");
         System.out.println(" see your transactions?" );




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
                Transaction transaction = new Transaction(dateTime,description, vendor, price);
                System.out.println(transaction.display());
                //System.out.println(transaction);
            }
            bufferedReader.close();

        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            //e.printStackTrace();
        } catch (
                NumberFormatException e) {
            System.out.println("Invalid number format in file.");
        }
        backToMainMenu();



    }
    public static void makePayment(){
         System.out.println("What would you like to do? ");
         System.out.println("------------------------");
         System.out.println("Do a payment ");
         scanner.nextInt();
         backToMainMenu();
         scanner.nextLine();
    }
    public static void ledger(){

    }

    public static void backToMainMenu() {
       System.out.println("press M to return to the main menu...");
       scanner.nextLine();
    }

}

