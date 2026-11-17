package com.theatre.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Booking entity representing a confirmed ticket reservation (FR-05, FR-08).
 * Demonstrates StringBuilder formatted receipt creation (Unit 4).
 */
public class Booking {
    private static int idCounter = 1000;

    private int bookingId;
    private Customer customer;
    private ShowSchedule show;
    private int numberOfTickets;
    private double totalAmount;
    private String bookingDate;
    private BookingStatus status;

    public Booking(Customer customer, ShowSchedule show, int numberOfTickets) {
        this.bookingId = ++idCounter;
        this.customer = customer;
        this.show = show;
        this.numberOfTickets = numberOfTickets;
        this.totalAmount = numberOfTickets * show.getTicketPrice();
        this.bookingDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.status = BookingStatus.CONFIRMED;
    }

    public int getBookingId() { return bookingId; }
    public Customer getCustomer() { return customer; }
    public ShowSchedule getShow() { return show; }
    public int getNumberOfTickets() { return numberOfTickets; }
    public double getTotalAmount() { return totalAmount; }
    public String getBookingDate() { return bookingDate; }
    public BookingStatus getStatus() { return status; }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    /**
     * Generate structured ticket receipt using StringBuilder (Unit 4: String Handling).
     */
    public String generateReceipt() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n=======================================================\n");
        sb.append("                 MOVIE TICKET RECEIPT                  \n");
        sb.append("=======================================================\n");
        sb.append(String.format(" Booking ID    : #%d\n", bookingId));
        sb.append(String.format(" Status        : %s\n", status));
        sb.append(String.format(" Booking Time  : %s\n", bookingDate));
        sb.append("-------------------------------------------------------\n");
        sb.append(String.format(" Customer Name : %s\n", customer.getName()));
        sb.append(String.format(" Phone Number  : %s\n", customer.getPhone()));
        sb.append("-------------------------------------------------------\n");
        sb.append(String.format(" Movie Title   : %s (%s)\n", show.getMovie().getTitle(), show.getMovie().getLanguage()));
        sb.append(String.format(" Screen        : %s\n", show.getScreen().getScreenName()));
        sb.append(String.format(" Show Schedule : %s at %s\n", show.getShowDate(), show.getShowTime()));
        sb.append(String.format(" Ticket Count  : %d ticket(s) @ ₹%.2f\n", numberOfTickets, show.getTicketPrice()));
        sb.append("-------------------------------------------------------\n");
        sb.append(String.format(" TOTAL AMOUNT  : ₹%.2f\n", totalAmount));
        sb.append("=======================================================\n");
        return sb.toString();
    }

    @Override
    public String toString() {
        return String.format("Booking #%d | Customer: %s | Movie: '%s' | Tickets: %d | Total: ₹%.2f | Status: %s",
                bookingId, customer.getName(), show.getMovie().getTitle(), numberOfTickets, totalAmount, status);
    }
}
