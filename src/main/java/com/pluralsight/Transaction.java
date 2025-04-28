package com.pluralsight;

public class Transaction {
private String date;
private String time;
private String description;
private String vendor;
private Float amount;

        public Transaction(String date, String time, String description, String vendor, Float amount){
            this.date = date;
            this.time = time;
            this.description = description;
            this. vendor = vendor;
            this.amount = amount;
        }

   // date = LocalDate.parse(input, DATE_FORMATTER);
   // dateTime = LocalDateTime.parse(input, TIME_FORMATTER);
}
