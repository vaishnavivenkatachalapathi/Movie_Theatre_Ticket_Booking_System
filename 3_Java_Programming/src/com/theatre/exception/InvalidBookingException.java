package com.theatre.exception;

/**
 * User-Defined Exception thrown for invalid input parameters or business rule violations (Unit 3).
 */
public class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}
