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

    public String getDescription() {
        return description;
    }

    public String getVendor() {
        return vendor;
    }

    public double getPrice() {
        return price;
    }

    public String display() {
        DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return "Date/Time: " + dateTime.format(displayFormatter) +
                "\nDescription: " + description +
                "\nVendor: " + vendor +
                "\nPrice: $" + String.format("%.2f", price);
    }

    @Override
    public String toString() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return String.join("|",
                dateTime.toLocalDate().format(dateFormatter),
                dateTime.toLocalTime().format(timeFormatter),
                description,
                vendor,
                String.valueOf(price));
    }
}