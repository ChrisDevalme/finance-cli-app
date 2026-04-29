package com.pluralsight;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

/*
 * Capstone skeleton – personal finance tracker.
 * ------------------------------------------------
 * File format  (pipe-delimited)
 *     yyyy-MM-dd|HH:mm:ss|description|vendor|amount
 * A deposit has a positive amount; a payment is stored
 * as a negative amount.
 */
public class FinancialTracker {

    /* ------------------------------------------------------------------
       Shared data and formatters
       ------------------------------------------------------------------ */
    private static final ArrayList<Transaction> transactions = new ArrayList<>();
    private static final String FILE_NAME = "transactions.csv";

    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private static final String TIME_PATTERN = "HH:mm:ss";
    private static final String DATETIME_PATTERN = DATE_PATTERN + " " + TIME_PATTERN;

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern(DATE_PATTERN);
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern(TIME_PATTERN);
    private static final DateTimeFormatter DATETIME_FMT = DateTimeFormatter.ofPattern(DATETIME_PATTERN);

    // Date Manipulation:
    private static final LocalDate today = LocalDate.now();
    private static final LocalDate previousMonthDay1 = today.minusMonths(1).withDayOfMonth(1);
    private static final LocalDate previousYearDay1 = today.minusYears(1).withDayOfYear(1);

    // Colors:
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String BLACK = "\u001B[30m";
    private static final String RESET = "\u001B[0m";
    private static final String BLUE = "\u001B[34m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE_BACKGROUND = "\u001B[44m";
    private static final String RED_BACKGROUND = "\u001B[41m";


    /* ------------------------------------------------------------------
       Main menu
       ------------------------------------------------------------------ */
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

    /* ------------------------------------------------------------------
       File I/O
       ------------------------------------------------------------------ */

    /**
     * Load transactions from FILE_NAME.
     * • If the file doesn’t exist, create an empty one so that future writes succeed.
     * • Each line looks like: date|time|description|vendor|amount
     */
    public static void loadTransactions(String fileName) {
        // TODO: create file if it does not exist, then read each line,
        //       parse the five fields, build a Transaction object,
        //       and add it to the transactions list.
        try{
        BufferedReader bf = new BufferedReader(new FileReader(fileName));
        String line;

        while((line = bf.readLine()) != null ) {
            String[] transactionLineItem = line.split("\\|");
            LocalDate transactionDate = LocalDate.parse(transactionLineItem[0]);
            LocalTime transactionTime = LocalTime.parse(transactionLineItem[1]);
            String transactionName = transactionLineItem[2];
            String transactionVendorName = transactionLineItem[3];
            double transactionValue = Double.parseDouble(transactionLineItem[4]);

            transactions.add(new Transaction(transactionDate, transactionTime, transactionName,
                    transactionVendorName, transactionValue));
        }
        bf.close();
        } catch (Exception e) {
            System.out.println("Invalid file. Try again.");
        }

    }

    /* ------------------------------------------------------------------
       Add new transactions
       ------------------------------------------------------------------ */

    /**
     * Prompt for ONE date+time string in the format
     * "yyyy-MM-dd HH:mm:ss", plus description, vendor, amount.
     * Validate that the amount entered is positive.
     * Store the amount as-is (positive) and append to the file.
     */
    private static void addDeposit(Scanner scanner) {
        // TODO
        System.out.println("Enter date of Transaction - (Format: yyyy-MM-dd Ex: 2026-04-21): ");
        String transactionDate = scanner.nextLine();
        System.out.println("Enter time of Transaction - (Format: HH:mm:ss Ex: 09:41:10): ");
        String transactionTime = scanner.nextLine();
        System.out.println("Enter name of Transaction: ");
        String transactionName = scanner.nextLine();
        System.out.println("Enter Location/Vendor of Transaction: ");
        String transactionLocation = scanner.nextLine();
        System.out.println("Enter amount of Transaction: ");
        double transactionAmount = scanner.nextDouble();
        scanner.nextLine();

        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(FILE_NAME, true));

            LocalDate transactionDateFMT = LocalDate.parse(transactionDate);
            LocalTime transactionTimeFMT = LocalTime.parse(transactionTime);
            if (transactionAmount > 0) {
                Transaction newTransaction = new Transaction(transactionDateFMT, transactionTimeFMT, transactionName, transactionLocation, transactionAmount);
                bufferedWriter.write(newTransaction + "\n");
                transactions.add(newTransaction);
                System.out.println("Transaction Recorded");
            } else {
                System.out.println("Enter Positive Numbers Only.");
            }
            bufferedWriter.close();

        } catch (Exception e) {
            System.out.println("Invalid File, try again.");
        }
    }

    /**
     * Same prompts as addDeposit.
     * Amount must be entered as a positive number,
     * then converted to a negative amount before storing.
     */
    private static void addPayment(Scanner scanner) {
        // TODO
        System.out.println("Enter date of Payment - (Format: yyyy-MM-dd Ex: 2026-04-21): ");
        String transactionDate = scanner.nextLine();
        System.out.println("Enter time of Payment - (Format: HH:mm:ss Ex: 09:41:10): ");
        String transactionTime = scanner.nextLine();
        System.out.println("Enter name of Payment: ");
        String transactionName = scanner.nextLine();
        System.out.println("Enter Location/Vendor of Payment: ");
        String transactionLocation = scanner.nextLine();
        System.out.println("Enter cost of Payment (Positive number's only): ");
        double transactionCost = scanner.nextDouble();

        scanner.nextLine();

        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(FILE_NAME, true));

            LocalDate transactionDateFMT = LocalDate.parse(transactionDate);
            LocalTime transactionTimeFMT = LocalTime.parse(transactionTime);
            if (transactionCost > 0) {
                Transaction newTransaction = new Transaction(transactionDateFMT, transactionTimeFMT, transactionName, transactionLocation, -transactionCost);
                bufferedWriter.write(newTransaction + "\n");
                transactions.add(newTransaction);
                System.out.println("Transaction Recorded.");
            } else {
                System.out.println("Enter Positive Numbers Only.");
            }
            bufferedWriter.close();

        } catch (Exception e) {
            System.out.println("Invalid File, try again.");
        }
    }


    /* ------------------------------------------------------------------
       Ledger menu
       ------------------------------------------------------------------ */
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

    /* ------------------------------------------------------------------
       Display helpers: show data in neat columns
       ------------------------------------------------------------------ */
    private static void displayLedger() {
        /* TODO – print all transactions in column format */
            System.out.printf("%-15s %-15s %-35s %-25s %-10s %n", "Date", "Time", "Description", "Vendor", "Amount");
            System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - " +
                    "- - - - - - - - - - - - - - - - - - - - ");
        for (Transaction transaction : transactions) {
            if (transaction.getTransactionAmount() > 0) {
                System.out.printf(GREEN + "%-15s %-15s %-35s %-25s %-10.2f %n" + RESET, transaction.getDate(), transaction.getTime(),
                        transaction.getTransactionName(), transaction.getTransactionLocation(), transaction.getTransactionAmount());
            }
            if(transaction.getTransactionAmount() < 0) {
                System.out.printf(RED + "%-15s %-15s %-35s %-25s %-10.2f %n" + RESET, transaction.getDate(), transaction.getTime(),
                        transaction.getTransactionName(), transaction.getTransactionLocation(), transaction.getTransactionAmount());
            }
        }

    }

    private static void displayDeposits() {
        /* TODO – only amount > 0               */
            System.out.printf(GREEN + "%-15s %-15s %-35s %-25s %-10s %n", "Date", "Time", "Description", "Vendor", "Amount" + RESET);
            System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - " +
                    "- - - - - - - - - - - - - - - - - - - - ");
        for (Transaction transaction : transactions) {
            if(transaction.getTransactionAmount() > 0) {
                System.out.printf("%-15s %-15s %-35s %-25s %-10.2f %n", transaction.getDate(), transaction.getTime(),
                        transaction.getTransactionName(), transaction.getTransactionLocation(), transaction.getTransactionAmount());
            }
        }
    }

    private static void displayPayments() {
        /* TODO – only amount < 0               */
            System.out.printf("%-15s %-15s %-35s %-25s %-10s %n", "Date", "Time", "Description", "Vendor", "Amount");
            System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - " +
                    "- - - - - - - - - - - - - - - - - - - - ");
        for (Transaction transaction : transactions) {
            if(transaction.getTransactionAmount() < 0) {
                System.out.printf(RED + "%-15s %-15s %-35s %-25s %-10.2f %n" + RESET, transaction.getDate(), transaction.getTime(),
                        transaction.getTransactionName(), transaction.getTransactionLocation(), transaction.getTransactionAmount());
            }
        }
    }

    /* ------------------------------------------------------------------
       Reports menu
       ------------------------------------------------------------------ */
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

            switch (input) {
                case "1" -> {filterTransactionsByDate(today.withDayOfMonth(1), today); }
                case "2" -> {filterTransactionsByDate(previousMonthDay1, previousMonthDay1.withDayOfMonth(previousMonthDay1.lengthOfMonth())); }
                case "3" -> {filterTransactionsByDate(today.withDayOfYear(1), today); }
                case "4" -> {filterTransactionsByDate(previousYearDay1, previousYearDay1.withMonth(12).withDayOfMonth(31));}
                case "5" -> {filterTransactionsByVendor(scanner); }
                case "6" -> customSearch(scanner);
                case "0" -> running = false;
                default -> System.out.println("Invalid option");
            }
        }
    }

    /* ------------------------------------------------------------------
       Reporting helpers
       ------------------------------------------------------------------ */
    private static void filterTransactionsByDate(LocalDate start, LocalDate end) {
        // TODO – iterate transactions, print those within the range

        System.out.printf("%-15s %-15s %-35s %-25s %-10s %n", "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - " +
                "- - - - - - - - - - - - - - - - - - - - ");
        for (Transaction transaction : transactions) {
            LocalDate transactionDate = transaction.getDate();
            if(!transactionDate.isBefore(start) && !transactionDate.isAfter(end)) {
                if (transaction.getTransactionAmount() > 0) {
                    System.out.printf(GREEN + "%-15s %-15s %-35s %-25s %-10.2f %n" + RESET, transaction.getDate(), transaction.getTime(),
                            transaction.getTransactionName(), transaction.getTransactionLocation(), transaction.getTransactionAmount());
                }
                if (transaction.getTransactionAmount() < 0) {
                    System.out.printf(RED + "%-15s %-15s %-35s %-25s %-10.2f %n" + RESET, transaction.getDate(), transaction.getTime(),
                            transaction.getTransactionName(), transaction.getTransactionLocation(), transaction.getTransactionAmount());
                }
            }
            
        }

    }

    private static void filterTransactionsByVendor(Scanner scanner) {
        // TODO – iterate transactions, print those with matching vendor
        System.out.println("Enter Vendor for search: ");
        String vendor = scanner.nextLine();
        for (Transaction transaction : transactions) {
            String transactionVendor = transaction.getTransactionLocation();
            if (transactionVendor.equalsIgnoreCase(vendor)) {
                System.out.println(transaction);
            }
        }
    }

    private static void customSearch(Scanner scanner) {
        // TODO – prompt for any combination of date range, description,
        //        vendor, and exact amount, then display matches
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

        System.out.printf("%-15s %-15s %-35s %-25s %-10s %n", "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - " +
                "- - - - - - - - - - - - - - - - - - - - ");
        for (Transaction transaction : transactions) {
            boolean matches = true;

            if (startDate != null && transaction.getDate().isBefore(startDate)) {
                matches = false;
            }

            if (endDate != null && transaction.getDate().isAfter(endDate)) {
                matches = false;
            }

            if (!descriptionInput.isEmpty() &&
                    !transaction.getTransactionName().toLowerCase().contains(descriptionInput.toLowerCase())) {
                matches = false;
            }

            if (!vendorInput.isEmpty() &&
                    !transaction.getTransactionLocation().toLowerCase().contains(vendorInput.toLowerCase())) {
                matches = false;
            }

            if (amountDouble != null && transaction.getTransactionAmount() != amountDouble) {
                matches = false;
            }

            if (matches) {
                if (transaction.getTransactionAmount() > 0) {
                System.out.printf(GREEN + "%-15s %-15s %-35s %-25s %-10.2f %n" + RESET, transaction.getDate(), transaction.getTime(),
                        transaction.getTransactionName(), transaction.getTransactionLocation(), transaction.getTransactionAmount());
                }
                if (transaction.getTransactionAmount() < 0) {
                System.out.printf(RED + "%-15s %-15s %-35s %-25s %-10.2f %n" + RESET, transaction.getDate(), transaction.getTime(),
                        transaction.getTransactionName(), transaction.getTransactionLocation(), transaction.getTransactionAmount());
                }
            }
        }
    }

    /* ------------------------------------------------------------------
       Utility parsers (you can reuse in many places)
       ------------------------------------------------------------------ */
    private static LocalDate parseDate(String s) {

        /* TODO – return LocalDate or null */
        LocalDate date = null;
        if (!s.isEmpty()) {
            date = LocalDate.parse(s);
        }

        return date;
    }

    private static Double parseDouble(String s) {
        /* TODO – return Double   or null */

        Double amount = null;
        if (!s.isEmpty()) {
            amount = Double.parseDouble(s);
        }

        return amount;
    }
}
