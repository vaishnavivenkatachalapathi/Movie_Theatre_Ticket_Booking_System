package com.theatre.exception;

/**
 * User-Defined Exception thrown when a referenced show ID does not exist.
 */
public class ShowNotFoundException extends Exception {
    public ShowNotFoundException(String message) {
        super(message);
    }
}
