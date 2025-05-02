package com.pluralsight;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class FinancialTracker {
    // Constants for file name and date/time formatting
    private static ArrayList<Transaction> transactions = new ArrayList<Transaction>();
    private static final String FILE_NAME = "transactions.csv";
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "HH:mm:ss";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_FORMAT);

    // Entry point of the application
    public static void main(String[] args) {
        loadTransactions(FILE_NAME);
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        // Main menu loop
        while (running) {
            System.out.println("Welcome to TransactionApp");
            System.out.println("Choose an option:");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment (Debit)");
            System.out.println("L) Ledger");
            System.out.println("X) Exit");

            String input = scanner.nextLine().trim();

            // Handle user input
            switch (input.toUpperCase()) {
                case "D":
                    addDeposit(scanner);
                    break;
                case "P":
                    addPayment(scanner);
                    break;
                case "L":
                    ledgerMenu(scanner);
                    break;
                case "X":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }


    }
    // Loads transactions from a file and populates the transactions list
    public static ArrayList<Transaction> loadTransactions(String fileName) {

       String line;
        try{
            BufferedReader br = new BufferedReader(new FileReader("transactions.csv"));
            while ((line = br.readLine()) != null){
                if (line.trim().isEmpty()) {
                    continue; // skip empty lines
                }
                String[] parts = line.split("\\|");
                if (parts.length != 5) {
                    System.out.println("Skipping bad line: " + line);
                    continue; // skip invalid lines
                }
                LocalDate date = LocalDate.parse(parts[0], DATE_FORMATTER);
                LocalTime time = LocalTime.parse(parts[1], TIME_FORMATTER);
                String description = parts[2];
                String vendor = parts[3];
                double amount = Double.parseDouble(parts[4]);
                transactions.add(new Transaction(date, time, description, vendor, amount));
            }
        } catch(Exception e){
            e.printStackTrace();
        }

    return transactions;
    }

    // Handles deposit input and saves it
    private static void addDeposit(Scanner scanner) {
        boolean rightAnswer = false;
       try {
           BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("transactions.csv", true));
           // Collect date, time, description, vendor, and amount from user
           System.out.println("Enter the date in format (yyyy-MM-dd): ");
           String unformattedDate = scanner.nextLine();
           LocalDate date = LocalDate.parse(unformattedDate, DATE_FORMATTER);
           System.out.println(date);

           System.out.println("Enter the time (HH:mm:ss): ");
           String unformattedTime = scanner.nextLine();
           LocalTime time = LocalTime.parse(unformattedTime, TIME_FORMATTER);
           System.out.println(time);

           System.out.println("Enter the descriptions: ");
           String answer1 = scanner.nextLine();
           System.out.println(answer1);

           System.out.println("Enter the vendor: ");
           String answer2 = scanner.nextLine();
           System.out.println(answer2);
           Double answer3 = null;
           while (!rightAnswer) {
               System.out.println("Enter the Amount of money: ");
               answer3 = scanner.nextDouble();
               scanner.nextLine();
               if (answer3 < 0) {
                   System.out.println("The amount is not positive try again");
               } else {
                   rightAnswer = true;

               }
               System.out.println(answer3);
           }
           // Create and store transaction
           Transaction transaction = new Transaction(date, time, answer1, answer2, answer3);
           transactions.add(transaction);

           bufferedWriter.write(transaction.toString());
           bufferedWriter.newLine();
           bufferedWriter.close();

       }catch(Exception e){
        e.printStackTrace();
        }
    }

    // Handles payment (negative amount) input and saves it
    private static void addPayment(Scanner scanner) {
        boolean rightAnswer = false;
        try{
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("transactions.csv", true));

        System.out.println("Enter the date in format (yyyy-MM-dd): ");
        String unformattedDate = scanner.nextLine();
        LocalDate date = LocalDate.parse(unformattedDate, DATE_FORMATTER);
        System.out.println(date);

        System.out.println("Enter the time (HH:mm:ss): ");
        String unformattedTime = scanner.nextLine();
        LocalTime time = LocalTime.parse(unformattedTime, TIME_FORMATTER);
        System.out.println(time);

        System.out.println("Enter the descriptions: ");
        String answer1 = scanner.nextLine();
        System.out.println(answer1);

        System.out.println("Enter the vendor: ");
        String answer2 = scanner.nextLine();
        System.out.println(answer2);
        Double answer3 = null;
        while (!rightAnswer) {
            System.out.println("Enter the Amount of money: ");
            answer3 = scanner.nextDouble();
            scanner.nextLine();

            if (answer3 < 0) {
                System.out.println("The amount must be greater than zero, try again");
            } else {
                answer3 *= -1;
                rightAnswer = true;

            }
            System.out.println(answer3);
        }

        Transaction transaction = new Transaction(date, time, answer1, answer2, answer3);
        transactions.add(transaction);

        bufferedWriter.write(transaction.toString());
        bufferedWriter.newLine();
        bufferedWriter.close();

    }catch(Exception e){
        e.printStackTrace();
    }


    }

    // Displays the ledger menu
    private static void ledgerMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("Ledger");
            System.out.println("Choose an option:");
            System.out.println("A) All");
            System.out.println("D) Deposits");
            System.out.println("P) Payments");
            System.out.println("R) Reports");
            System.out.println("H) Home");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "A":
                    displayLedger();
                    break;
                case "D":
                    displayDeposits();
                    break;
                case "P":
                    displayPayments();
                    break;
                case "R":
                    reportsMenu(scanner);
                    break;
                case "H":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }
    // Displays all transactions
    private static void displayLedger() {
        System.out.printf("%-12s %-10s %-30s %-25s %10s\n", "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("-----------------------------------------------------------------------------------------");
        for (Transaction transaction : transactions) {
            System.out.println(transaction);
        }
    }

    // Displays only deposits (positive amounts)
    private static void displayDeposits() {
        System.out.printf("%-12s %-10s %-30s %-25s %10s\n", "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("----------------------------------------------------------------------------------------------");
        for (Transaction transaction : transactions) {
            if(transaction.getAmount() > 0){
                System.out.println(transaction.toString());
            }
        }


    }

    // Displays only payments (negative amounts)
    private static void displayPayments() {
        System.out.printf("%-12s %-10s %-30s %-25s %10s\n", "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("----------------------------------------------------------------------------------------------");
        for (Transaction transaction : transactions) {
            if (transaction.getAmount() < 0) {
                System.out.println(transaction.toString());
            }
        }
    }

    // Displays the reports menu and handles each report type
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
            System.out.println("6) Filter by Search");
            System.out.println("0) Back");

            String input = scanner.nextLine().trim();
            LocalDate today = LocalDate.now();
            LocalDate startDate;
            LocalDate endDate;
            switch (input) {
                case "1":

                    startDate = today.withDayOfMonth(1);
                    endDate = today;
                    filterTransactionsByDate(startDate,endDate);
                    break;
                case "2":

                    System.out.println("Enter a date (yyyy-MM-dd) to get transaction from previous month");
                    input = scanner.nextLine().trim();

                    LocalDate userDate = LocalDate.parse(input);
                    LocalDate previousMonthDate = userDate.minusMonths(1);
                    startDate = previousMonthDate.withDayOfMonth(1);
                    endDate =  previousMonthDate.withDayOfMonth(previousMonthDate.lengthOfMonth());
                    filterTransactionsByDate(startDate, endDate);
                    break;
                case "3":

                     today = LocalDate.now();
                     startDate = LocalDate.of(today.getYear(),1,1);
                     endDate = today;
                    filterTransactionsByDate(startDate, endDate);
                    break;
                case "4":


                   // System.out.println("Enter a date (yyyy-MM-dd) to get transaction from previous year");
                    //input = scanner.nextLine().trim();

                     //userDate = LocalDate.parse(input,DATE_FORMATTER);
                    userDate = today;
                      startDate = userDate.minusYears(1);
                    //startDate = LocalDate.of(Date,1,1);
                    int prev = startDate.getYear();
                    startDate = LocalDate.of(prev, 1,1);
                     endDate = LocalDate.of(prev,12, 31);
                    filterTransactionsByDate(startDate, endDate);

                    break;
                case "5":
                    System.out.println("Enter the vendor that you would like to search for? ");
                    String answer = scanner.nextLine();
                    filterTransactionsByVendor(answer);
                    break;
                case "6":
                    FilterSearch(scanner);
                    break;
                case "0":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option");
                    break;

            }


        }
    }

    // Filters and prints transactions within a given date range
    private static void filterTransactionsByDate(LocalDate startDate, LocalDate endDate) {
        System.out.printf("%-12s %-10s %-30s %-25s %10s\n", "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("----------------------------------------------------------------------------------------------");

        for (Transaction transaction : transactions) {
            LocalDate date = transaction.getDate();
          if ((date.isEqual(startDate) || date.isAfter(startDate)) &&
                    (date.isEqual(endDate) || date.isBefore(endDate))) {
                System.out.println(transaction);
            }


        }
    }

    /*   public static void currentYear(){
        LocalDate today = LocalDate.now();
         LocalDate startDate = LocalDate.of(today.getYear(),1,1);
         LocalDate endDate = today;
        for (Transaction transaction : transactions) {
            LocalDate transDate = transaction.getDate();

            if(transDate.getYear() == currentYear){
                System.out.println(transaction);
            }
        }


    } */

    /*public static void CurrentMonth(){

        LocalDate today = LocalDate.now();
        int currentMonth = today.getMonthValue();
        int currentYear = today.getYear();

        for (Transaction transaction : transactions) {
            LocalDate transDate = transaction.getDate();

            if(transDate.getYear() == currentYear && transDate.getMonthValue() == currentMonth){
                System.out.println(transaction);
            }
        }
        }
*/


 /*   public static void previousMonth(){
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter a date (yyyy-MM-dd) to get transaction from previous month");
        String input = scanner.nextLine().trim();

        LocalDate userDate = LocalDate.parse(input);
        LocalDate previousMonthDate = userDate.minusMonths(1);
        startDate = previousMonthDate.withDayOfMonth(1);
        endDate =  previousMonthDate.withDayOfMonth(previousMonthDate.lengthOfMonth());
        int targetMonth = previousMonthDate.getMonthValue();

        for (Transaction transaction : transactions) {
            LocalDate transactionDate = transaction.getDate();
            if( transactionDate.getMonthValue()==targetMonth){
                System.out.println(transaction);
            }
        }

    }
    public static void previousYear(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a date (yyyy-MM-dd) to get transaction from previous year");
        String input = scanner.nextLine().trim();

        LocalDate userDate = LocalDate.parse(input);
        LocalDate previousYearDate = userDate.minusYears(1);
        endDate = LocalDate.of(previousYearDate.getYear(),12, 31);
        int targetYear = previousYearDate.getYear();

        for (Transaction transaction : transactions) {
            LocalDate transactionDate = transaction.getDate();
            if(transactionDate.getYear() == targetYear){
                System.out.println(transaction);
            }
        }

    } */


    // Filters transactions by vendor name
    private static void filterTransactionsByVendor(String vendor) {
        System.out.printf("%-12s %-10s %-30s %-25s %10s\n", "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("----------------------------------------------------------------------------------------------");

        for (Transaction transaction : transactions) {
            if(transaction.getVendor().equalsIgnoreCase(vendor)){
                System.out.println(transaction);
            }
        }
    }


    /*Challenge Yourself
If you have time and want to challenge yourself, consider the following:
On the reports screen add another option for a custom search. Prompt the user
for search values for all ledger entry properties.
3
• 6) Custom Search - prompt the user for the following search values.
o Start Date
o End Date
o Description
o Vendor
o Amount
• If the user enters a value for a field you should filter on that field
• If the user does not enter a value, you should not filter on that field

     */
    // Allows the user to search using multiple optional filters
    public static void FilterSearch(Scanner scanner){
        System.out.printf("%-12s %-10s %-30s %-25s %10s\n", "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("----------------------------------------------------------------------------------------------");

        System.out.println("Enter search filters (press Enter to skip a field):");

        System.out.print("Start Date (yyyy-MM-dd): ");
        String startDateInput = scanner.nextLine().trim();

        System.out.print("End Date (yyyy-MM-dd): ");
        String endDateInput = scanner.nextLine().trim();

        System.out.print("Description: ");
        String descriptionInput = scanner.nextLine().trim().toLowerCase();

        System.out.print("Vendor: ");
        String vendorInput = scanner.nextLine().trim().toLowerCase();

        System.out.print("Amount (exact match): ");
        String amountInput = scanner.nextLine().trim();


        LocalDate startDate = null;
        LocalDate endDate = null;
        Double amount = null;
        String vendor = null;
        String description = null;
        try {
            if (!startDateInput.isEmpty()) {
                startDate = LocalDate.parse(startDateInput);
            }
            if (!endDateInput.isEmpty()) {
                endDate = LocalDate.parse(endDateInput);
            }
            if (!amountInput.isEmpty()) {
                amount = Double.parseDouble(amountInput);
            }
            if (!vendorInput.isEmpty()) {
                vendor = vendorInput;
            }
            if(!descriptionInput.isEmpty()) {
                description = descriptionInput;
            }

        } catch (Exception e) {
            System.out.println("Invalid input format. Search aborted.");
            return;
        }
        // Perform filtering
        System.out.println("\nFiltered Results:");
        boolean found = false;

        for (Transaction t : transactions) {
            boolean matches = true;

            if (startDate != null && t.getDate().isBefore(startDate))
                matches = false;
            if (endDate != null && t.getDate().isAfter(endDate))
                matches = false;
            if (!descriptionInput.isEmpty() && !t.getDescription().toLowerCase().contains(descriptionInput))
                matches = false;
            if (!vendorInput.isEmpty() && !t.getVendor().toLowerCase().contains(vendorInput))
                matches = false;
            if (amount != null && t.getAmount() != amount)
                matches = false;

            if (matches) {
                System.out.println(t);
                found = true;
            }
        }


   }


}
