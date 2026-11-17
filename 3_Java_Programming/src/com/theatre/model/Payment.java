package com.theatre.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Payment model entity representing payment processing.
 */
public class Payment {
    private static int idCounter = 4000;

    private int paymentId;
    private int bookingId;
    private String paymentMode;
    private double amount;
    private String paymentStatus;
    private String transactionTime;

    public Payment(int bookingId, String paymentMode, double amount) {
        this.paymentId = ++idCounter;
        this.bookingId = bookingId;
        this.paymentMode = paymentMode;
        this.amount = amount;
        this.paymentStatus = "SUCCESS";
        this.transactionTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public int getPaymentId() { return paymentId; }
    public int getBookingId() { return bookingId; }
    public String getPaymentMode() { return paymentMode; }
    public double getAmount() { return amount; }
    public String getPaymentStatus() { return paymentStatus; }
    public String getTransactionTime() { return transactionTime; }

    public void setPaymentStatus(String status) { this.paymentStatus = status; }
}
