package com.theatre.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Cancellation entity representing a cancelled booking and calculated refund (FR-07).
 */
public class Cancellation {
    private static int idCounter = 3000;

    private int cancellationId;
    private Booking booking;
    private String cancellationDate;
    private double refundAmount;
    private double cancellationFee;

    public Cancellation(Booking booking, double refundPercentage) {
        this.cancellationId = ++idCounter;
        this.booking = booking;
        this.cancellationDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.refundAmount = booking.getTotalAmount() * (refundPercentage / 100.0);
        this.cancellationFee = booking.getTotalAmount() - this.refundAmount;
    }

    public int getCancellationId() { return cancellationId; }
    public Booking getBooking() { return booking; }
    public String getCancellationDate() { return cancellationDate; }
    public double getRefundAmount() { return refundAmount; }
    public double getCancellationFee() { return cancellationFee; }

    public String generateCancellationSlip() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n=======================================================\n");
        sb.append("             CANCELLATION & REFUND SLIP                \n");
        sb.append("=======================================================\n");
        sb.append(String.format(" Cancellation ID : #%d\n", cancellationId));
        sb.append(String.format(" Booking ID Ref  : #%d\n", booking.getBookingId()));
        sb.append(String.format(" Timestamp       : %s\n", cancellationDate));
        sb.append("-------------------------------------------------------\n");
        sb.append(String.format(" Customer        : %s\n", booking.getCustomer().getName()));
        sb.append(String.format(" Movie Cancelled : %s\n", booking.getShow().getMovie().getTitle()));
        sb.append(String.format(" Seats Released  : %d ticket(s)\n", booking.getNumberOfTickets()));
        sb.append(String.format(" Original Paid   : ₹%.2f\n", booking.getTotalAmount()));
        sb.append(String.format(" Cancellation Fee: ₹%.2f\n", cancellationFee));
        sb.append("-------------------------------------------------------\n");
        sb.append(String.format(" REFUND DISBURSED: ₹%.2f\n", refundAmount));
        sb.append("=======================================================\n");
        return sb.toString();
    }
}
