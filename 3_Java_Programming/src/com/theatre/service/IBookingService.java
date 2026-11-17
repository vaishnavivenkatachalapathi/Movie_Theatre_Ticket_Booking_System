package com.theatre.service;

import com.theatre.model.*;
import com.theatre.exception.*;
import java.util.List;

/**
 * Interface defining contract for Ticket Booking and Show Management (Unit 2: Interfaces).
 */
public interface IBookingService {
    void addMovie(Movie movie);
    void addScreen(Screen screen);
    void scheduleShow(ShowSchedule show);
    
    List<Movie> getAllMovies();
    List<Screen> getAllScreens();
    List<ShowSchedule> getAllShows();
    ShowSchedule getShowById(int showId) throws ShowNotFoundException;

    Booking bookTicket(int showId, Customer customer, int numberOfTickets) 
            throws ShowNotFoundException, SeatUnavailableException, InvalidBookingException;

    Cancellation cancelBooking(int bookingId, double leadTimeHours) 
            throws InvalidBookingException;

    Booking getBookingById(int bookingId) throws InvalidBookingException;
    List<Booking> getAllBookings();
}
