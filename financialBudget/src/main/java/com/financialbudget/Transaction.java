package com.financialbudget;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {
    private LocalDateTime dateTime;
    private String description;
    private String vendor;
    private double price;
    public Transaction(LocalDateTime dateTime, String description, String vendor, double price) {
        this.dateTime = dateTime;
        this.description = description;
        this.vendor = vendor;
        this.price = price;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String display() {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("E, MMM dd, yyyy hh:mm a");
        StringBuilder builder = new StringBuilder();
        builder.append(dateTime.format(format)).append(" ").append("\n")
                .append(description).append("\n")
                .append(vendor).append("\n")
                .append(price);
        return builder.toString();

    }

    @Override
    public String toString() {
        DateTimeFormatter dateFormatForFile = DateTimeFormatter.ofPattern("yyyy-MM-dd|HH:mm:ss");
        String transaction = String.format("%s|%s|%s|%.2f", dateTime.format(dateFormatForFile), description,vendor,price);
        return transaction;
    }
}

