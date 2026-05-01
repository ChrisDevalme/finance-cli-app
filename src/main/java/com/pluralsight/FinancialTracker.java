package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class FinancialTracker {

    private static final ArrayList<Transaction> transactions = new ArrayList<>();
    private static final String FILE_NAME = "transactions.csv";

    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private static final String TIME_PATTERN = "HH:mm:ss";
    private static final String DATETIME_PATTERN = DATE_PATTERN + " " + TIME_PATTERN;

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern(DATE_PATTERN);
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern(TIME_PATTERN);
    private static final DateTimeFormatter DATETIME_FMT = DateTimeFormatter.ofPattern(DATETIME_PATTERN);

    // Colors:
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String BLACK = "\u001B[30m";
    private static final String RESET = "\u001B[0m";
    private static final String BLUE = "\u001B[34m";
    private static final String BLUE_BACKGROUND = "\u001B[44m";
    private static final String RED_BACKGROUND = "\u001B[41m";


    public static void main(String[] args) {
        loadTransactions(FILE_NAME);

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println(BLUE_BACKGROUND + BLACK + "Welcome to TransactionApp" + RESET);
            System.out.println(BLUE_BACKGROUND + BLACK + "Choose an option:" + RESET);
            System.out.println(GREEN + "D) Add Deposit" + RESET);
            System.out.println(RED + "P) Make Payment (Debit)" + RESET);
            System.out.println(BLUE + "L) Ledger" + RESET);
            System.out.println(RED_BACKGROUND + BLACK + "X) Exit" + RESET);

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "D" -> addDeposit(scanner);
                case "P" -> addPayment(scanner);
                case "L" -> ledgerMenu(scanner);
                case "X" -> running = false;
                default -> System.out.println("Invalid option");
            }
        }
        scanner.close();
    }
    /**
     * Loads transactions from the given file into the transactions list.

     * If the file does not exist, a new file is created.
     * Each valid line is split using the pipe character and converted
     * into a Transaction object.
     *
     * @param fileName the name of the transaction file to load
     */
    public static void loadTransactions(String fileName) {

        try {
            // Check if the transaction file exists
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
                System.out.println("File did not exist. New file created: " + fileName);
                return;
            }
            BufferedReader bf = new BufferedReader(new FileReader(fileName));
            String line;

            while((line = bf.readLine()) != null ) {
                // Split the line into fields using the pipe
                String[] transactionLineItem = line.split("\\|");
                LocalDate date = LocalDate.parse(transactionLineItem[0]);
                LocalTime time = LocalTime.parse(transactionLineItem[1]);
                String description = transactionLineItem[2];
                String vendor = transactionLineItem[3];
                double amount = Double.parseDouble(transactionLineItem[4]);
                // Add the loaded transaction to the transactions list
                transactions.add(new Transaction(date, time, description,
                        vendor, amount));
            }
            bf.close();
            // Sort transactions by date first, then by time
            Collections.sort(transactions, Comparator.comparing(Transaction::getDate).thenComparing(Transaction::getTime));
        } catch (Exception e) {
            System.out.println("File not found, new file created:");
        }
    }
    /**
     * Adds a deposit transaction after collecting valid input from the user.
     *
     * @param scanner used to read user input from the console
     */
    private static void addDeposit(Scanner scanner) {
        LocalDate date = null;
        LocalTime time = null;
        String description = "";
        String vendor = "";
        double amount = 0;
        // Keep asking until the user enters a valid date
        while (date == null) {
            System.out.print("Enter date of transaction (yyyy-MM-dd, example: 2026-04-21): ");
            String input = scanner.nextLine().trim();

            try {
                date = LocalDate.parse(input, DATE_FMT);
            } catch (Exception e) {
                System.out.println("Invalid date format. Please try again.");
            }
        }

        while (time == null) {
            System.out.print("Enter time of transaction (HH:mm:ss, example: 09:41:10): ");
            String input = scanner.nextLine().trim();

            try {
                time = LocalTime.parse(input, TIME_FMT);
            } catch (Exception e) {
                System.out.println("Invalid time format. Please try again.");
            }
        }

        while (description.isBlank()) {
            System.out.print("Enter description of transaction: ");
            description = scanner.nextLine().trim();

            if (description.isBlank()) {
                System.out.println("Transaction description cannot be empty.");
            }
        }

        while (vendor.isBlank()) {
            System.out.print("Enter vendor of transaction: ");
            vendor = scanner.nextLine().trim();

            if (vendor.isBlank()) {
                System.out.println("Vendor cannot be empty.");
            }
        }
        boolean validAmount = false;
        // Keep asking until the user enters a valid positive amount
        while (!validAmount) {
            System.out.print("Enter amount of transaction: ");
            String input = scanner.nextLine().trim();

            try {
                amount = Double.parseDouble(input);

                if (amount > 0) {
                    validAmount = true;
                } else {
                    System.out.println("Enter a positive number only.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid amount. Please enter a numeric value.");
            }
        }
        Transaction newTransaction = new Transaction(date, time, description, vendor, amount);

        saveTransaction(newTransaction);
    }
    /**
     * Adds a payment transaction after collecting valid input from the user.

     * The amount is stored as a negative number because payments reduce the balance.
     *
     * @param scanner used to read user input from the console
     */
    private static void addPayment(Scanner scanner) {
        LocalDate date = null;
        LocalTime time = null;
        String description = "";
        String vendor = "";
        double amount = 0;

        while (date == null) {
            System.out.print("Enter date of transaction (yyyy-MM-dd, example: 2026-04-21): ");
            String input = scanner.nextLine().trim();

            try {
                date = LocalDate.parse(input, DATE_FMT);
            } catch (Exception e) {
                System.out.println("Invalid date format. Please try again.");
            }
        }

        while (time == null) {
            System.out.print("Enter time of transaction (HH:mm:ss, example: 09:41:10): ");
            String input = scanner.nextLine().trim();

            try {
                time = LocalTime.parse(input, TIME_FMT);
            } catch (Exception e) {
                System.out.println("Invalid time format. Please try again.");
            }
        }

        while (description.isBlank()) {
            System.out.print("Enter description of transaction: ");
            description = scanner.nextLine().trim();

            if (description.isBlank()) {
                System.out.println("Transaction description cannot be empty.");
            }
        }

        while (vendor.isBlank()) {
            System.out.print("Enter vendor of transaction: ");
            vendor = scanner.nextLine().trim();

            if (vendor.isBlank()) {
                System.out.println("Vendor cannot be empty.");
            }
        }

        boolean validAmount = false;
        while (!validAmount) {
            System.out.print("Enter amount of transaction: ");
            String input = scanner.nextLine().trim();

            try {
                amount = Double.parseDouble(input);
                if (amount > 0) {
                    validAmount = true;
                } else {
                    System.out.println("Enter a positive number only.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid amount. Please enter a numeric value.");
            }
        }
        Transaction newTransaction = new Transaction(date, time, description, vendor, -amount);
        saveTransaction(newTransaction);
    }
    /**
     * Saves a new transaction to the transaction file and adds it
     * to the transactions list.
     *
     * @param newTransaction the transaction that will be saved
     */
    private static void saveTransaction(Transaction newTransaction) {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(FILE_NAME, true));
            bufferedWriter.write(newTransaction.toString());
            bufferedWriter.newLine();
            transactions.add(newTransaction);
            System.out.println("Transaction recorded successfully.");
            bufferedWriter.close();
        } catch (Exception e) {
            System.out.println("Error writing transaction to file.");
        }
    }
    /**
     * Displays the ledger menu and lets the user choose which type of
     * transactions they want to view.
     *
     * @param scanner used to read menu choices from the user
     */
    private static void ledgerMenu(Scanner scanner) {

        boolean running = true;
        while (running) {
            System.out.println("Ledger");
            System.out.println("Choose an option:");
            System.out.println(BLUE + "A) All" + RESET);
            System.out.println(GREEN + "D) Deposits" + RESET);
            System.out.println(RED + "P) Payments" + RESET);
            System.out.println("R) Reports");
            System.out.println("H) Home");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "A" -> displayLedger();
                case "D" -> displayDeposits();
                case "P" -> displayPayments();
                case "R" -> reportsMenu(scanner);
                case "H" -> running = false;
                default -> System.out.println("Invalid option");
            }
        }
    }
    /**
     * Displays all transactions in the ledger.

     * Transactions are sorted by date and time before being printed.
     * Positive amounts are displayed as deposits, and negative amounts
     * are displayed as payments.
     */
    private static void displayLedger() {
        printTransactionHeader();
        for (Transaction transaction : transactions) {
           printColoredTransactions(transaction);
        }
    }
    /**
     * Displays only deposit transactions.

     * Deposits are transactions with an amount greater than zero.
     */
    private static void displayDeposits() {

        printTransactionHeader();
        for (Transaction transaction : transactions) {
            // Only print transactions with a positive amount
            if(transaction.getAmount() > 0) {
                printColoredTransactions(transaction);
            }
        }
    }
    /**
     * Displays only payment transactions.

     * Payments are transactions with an amount less than zero.
     */
    private static void displayPayments() {
        printTransactionHeader();
        for (Transaction transaction : transactions) {
            if(transaction.getAmount() < 0) {
                printColoredTransactions(transaction);
            }
        }
    }
    /**
     * Displays the reports menu and lets the user choose a report type.
     *
     * @param scanner used to read menu choices from the user
     */
    private static void reportsMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("Reports");
            System.out.println("Choose an option:");
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year To Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("6) Custom Search");
            System.out.println("0) Back");

            String input = scanner.nextLine().trim();

            LocalDate today = LocalDate.now();
            switch (input) {
                // Get the first day of the current month
                case "1" -> {filterTransactionsByDate(today.withDayOfMonth(1), today); }
                case "2" -> {
                    // Get the first day of the previous month
                    LocalDate previousMonthDay1 = today.minusMonths(1).withDayOfMonth(1);
                    filterTransactionsByDate(previousMonthDay1, previousMonthDay1.withDayOfMonth(previousMonthDay1.lengthOfMonth())); }
                case "3" -> {filterTransactionsByDate(today.withDayOfYear(1), today); }
                case "4" -> {
                    // Get the first day of the previous year
                    LocalDate previousYearDay1 = today.minusYears(1).withDayOfYear(1);
                    filterTransactionsByDate(previousYearDay1, previousYearDay1.withMonth(12).withDayOfMonth(31));}
                case "5" -> {filterTransactionsByVendor(scanner); }
                case "6" -> customSearch(scanner);
                case "0" -> running = false;
                default -> System.out.println("Invalid option");
            }
        }
    }
    /**
     * Displays transactions that fall within the given date range.
     *
     * @param start the first date allowed in the report
     * @param end the last date allowed in the report
     */
    private static void filterTransactionsByDate(LocalDate start, LocalDate end) {
        printTransactionHeader();
        for (Transaction transaction : transactions) {
            // Get the date of the current transaction
            LocalDate transactionDate = transaction.getDate();

            // Check if the transaction date is between start and end, including both dates
            if(!transactionDate.isBefore(start) && !transactionDate.isAfter(end)) {
                printColoredTransactions(transaction);
            }
        }
    }
    /**
     * Searches and displays transactions for a specific vendor.
     *
     * @param scanner used to read the vendor name from the user
     */
    private static void filterTransactionsByVendor(Scanner scanner) {

        System.out.println("Enter Vendor for search: ");
        String vendor = scanner.nextLine();
        printTransactionHeader();
        boolean isFound = false;

        for (Transaction transaction : transactions) {
            String transactionVendor = transaction.getVendor();
            if (transactionVendor.equalsIgnoreCase(vendor)) {
                printColoredTransactions(transaction);
                isFound = true;
            }
        }
        if (!isFound) {
            System.out.println("Vendor not found.");
        }
    }
    /**
     * Allows the user to search transactions using optional filters.

     * The user can search by start date, end date, description, vendor,
     * and amount. Blank inputs are ignored.
     *
     * @param scanner used to read search criteria from the user
     */
    private static void customSearch(Scanner scanner) {

        System.out.print("Start date (yyyy-MM-dd) (blank = any): ");
        String startInput = scanner.nextLine().trim();
        LocalDate startDate = parseDate(startInput);
        System.out.print("End date (yyyy-MM-dd) (blank = any): ");
        String endInput = scanner.nextLine().trim();
        LocalDate endDate = parseDate(endInput);
        System.out.print("Description (blank = any): ");
        String descriptionInput = scanner.nextLine().trim();
        System.out.print("Vendor (blank = any): ");
        String vendorInput = scanner.nextLine().trim();
        System.out.print("Amount (blank = any): ");
        String amountInput = scanner.nextLine().trim();
        Double amountDouble = parseDouble(amountInput);

        printTransactionHeader();
        boolean foundATransaction = false;
        for (Transaction transaction : transactions) {
            // Start by assuming the transaction matches the search
            // If any filter does not match, change matches to false
            boolean matches = true;

            // Check if the user entered a start date and the transaction is before it
            if (startDate != null && transaction.getDate().isBefore(startDate)) {
                matches = false;
            }
            // Check if the user entered an end date and the transaction is after it
            if (endDate != null && transaction.getDate().isAfter(endDate)) {
                matches = false;
            }
            // Check if the user entered a description and it does not match
            if (!descriptionInput.isEmpty() &&
                    !transaction.getDescription().toLowerCase().contains(descriptionInput.toLowerCase())) {
                matches = false;
            }
            // Check if the user entered a vendor and it does not match
            if (!vendorInput.isEmpty() &&
                    !transaction.getVendor().toLowerCase().contains(vendorInput.toLowerCase())) {
                matches = false;
            }
            // Check if the user entered an amount and it does not match
            if (amountDouble != null && transaction.getAmount() != amountDouble) {
                matches = false;
            }

            if (matches) {
                printColoredTransactions(transaction);
                foundATransaction = true;
            }
        }
            if(!foundATransaction) {
                System.out.println("No transactions found.");
            }
    }
    /**
     * Converts a String into a LocalDate.

     * If the String is empty, the method returns null.
     *
     * @param s the date input from the user
     * @return the parsed LocalDate, or null if the input is blank
     */
    private static LocalDate parseDate(String s) {

        LocalDate date = null;
        if (!s.isEmpty()) {
            date = LocalDate.parse(s);
        }

        return date;
    }
    /**
     * Converts a String into a Double.

     * If the String is empty, the method returns null.
     *
     * @param s the amount input from the user
     * @return the parsed Double, or null if the input is blank
     */
    private static Double parseDouble(String s) {

        Double amount = null;
        if (!s.isEmpty()) {
            amount = Double.parseDouble(s);
        }
        return amount;
    }
    /**
     * Prints one transaction using color formatting.

     * Deposits are printed in green and payments are printed in red.
     *
     * @param transaction the transaction to print
     */
    private static void printColoredTransactions(Transaction transaction){
        if (transaction.getAmount() > 0) {
            System.out.printf(GREEN + "%-15s %-15s %-35s %-25s %-10.2f %n" + RESET, transaction.getDate(), transaction.getTime(),
                    transaction.getDescription(), transaction.getVendor(), transaction.getAmount());
        }
        if(transaction.getAmount() < 0) {
            System.out.printf(RED + "%-15s %-15s %-35s %-25s %-10.2f %n" + RESET, transaction.getDate(), transaction.getTime(),
                    transaction.getDescription(), transaction.getVendor(), transaction.getAmount());
        }
    }
    /**
     * Prints the table header for displaying transaction information.
     */
    private static void printTransactionHeader() {
        System.out.printf("%-15s %-15s %-35s %-25s %-10s %n", "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - " +
                "- - - - - - - - - - - - - - - - - - - - ");
    }
}
