package com.financialbudget;

import java.io.FileReader;
import java.util.Scanner;

public class App {
     private static Scanner scanner = new Scanner(System.in);

     public static void main(String[] args) {

        boolean running = true;
        while (running){
            showMainMenu();
            int selectMenuOption = scanner.nextInt();
            scanner.nextLine();


            switch (selectMenuOption){
                case 1:
                    viewDeposit();
                    break;
                case 2:
                    makePayment();
                    break;
                case 3:
                    ledger();
                    break;
                case 4:
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
        System.out.println("1- Add Deposit");
        System.out.println("2- Make Payment (Debit)");
        System.out.println("3- Ledger");
        System.out.println("4- return to home screen");
        System.out.print("Please choose an option: ");
    }
    public static void viewDeposit(){
         System.out.println("Add deposit ");
        double amount = scanner.nextDouble();
         System.out.println("Your deposit of: " + amount + " was successful");
         System.out.println("Press 4 to go the main menu");
         scanner.nextInt();
    }
    public static void makePayment(){
         System.out.println("What would you like to do? ");
         System.out.println("Go into your debit information press (1) ");
         System.out.println("Do a payment press (2) ");
         boolean running = true;
         while (running){
              showInformation();
              int selectOptionReports = scanner.nextInt();
              switch (selectOptionReports){
                  case 1:
                      debitInformation();
                      break;
                  case 2:
                      payment();
                      break;

                  case 4:
                      running =false;
                      break;
                  default:
                      System.out.println("Invalid report menu option. Try again.");
              }
         }

    }
    public static void showInformation(){

    }
    public static void payment(){
        System.out.println("Enter payment amount: ");
        double paymentAmount = scanner.nextDouble();
        System.out.println("Your payment of: -" + paymentAmount + " was successful ");
        System.out.println("Press 4 to go the main menu ");
        scanner.nextLine();
    }
    public static void debitInformation(){
         System.out.println("Go to your file information ");

         FileReader fileReader = new FileReader("Data/transaction.csv");
    }
    public static void ledger(){

    }
}
