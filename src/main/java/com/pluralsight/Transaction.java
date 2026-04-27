package com.pluralsight;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Transaction {
    private LocalDateTime dateTime;
    private String transactionName;
    private double transactionAmount;

    public Transaction(LocalDateTime dateTime, String transactionName, double transactionAmount) {
        this.dateTime = dateTime;
        this.transactionName = transactionName;
        this.transactionAmount = transactionAmount;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getTransactionName() {
        return transactionName;
    }

    public void setTransactionName(String transactionName) {
        this.transactionName = transactionName;
    }

    public double getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(double transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    @Override
    public String toString() {
        return "Transaction - " +
                "Date/Time: " + dateTime + " | " +
                "Name: '" + transactionName + '\'' + " | " +
                "Amount: $" + transactionAmount;
    }
}
