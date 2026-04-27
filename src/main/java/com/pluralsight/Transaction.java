package com.pluralsight;

import java.time.LocalDate;
import java.time.LocalTime;

public class Transaction {
    private LocalDate date;
    private LocalTime time;
    private String transactionName;
    private String transactionLocation;
    private double transactionAmount;

    public Transaction(LocalDate date, LocalTime time, String transactionName, String transactionLocation, double transactionAmount) {
        this.date = date;
        this.time = time;
        this.transactionName = transactionName;
        this.transactionLocation = transactionLocation;
        this.transactionAmount = transactionAmount;
    }

    public String getTransactionLocation() {
        return transactionLocation;
    }

    public void setTransactionLocation(String transactionLocation) {
        this.transactionLocation = transactionLocation;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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
        return  date + "|" + time + "|" +
                transactionName + "|" + transactionLocation
                + "|" + transactionAmount;
    }
}
