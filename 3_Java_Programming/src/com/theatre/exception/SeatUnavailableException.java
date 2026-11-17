package com.theatre.exception;

/**
 * User-Defined Exception thrown when a booking exceeds available capacity (Unit 3, FR-06).
 */
public class SeatUnavailableException extends Exception {
    private int requestedSeats;
    private int availableSeats;

    public SeatUnavailableException(String message, int requestedSeats, int availableSeats) {
        super(message);
        this.requestedSeats = requestedSeats;
        this.availableSeats = availableSeats;
    }

    public int getRequestedSeats() { return requestedSeats; }
    public int getAvailableSeats() { return availableSeats; }
}
