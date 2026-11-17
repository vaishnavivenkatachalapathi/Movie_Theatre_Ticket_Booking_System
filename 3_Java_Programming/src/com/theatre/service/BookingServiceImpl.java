package com.theatre.service;

import com.theatre.model.*;
import com.theatre.exception.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service implementation managing movies, screens, shows, bookings, and cancellations.
 * Demonstrates Collections Framework, Lambdas, and Stream API (Unit 5).
 */
public class BookingServiceImpl implements IBookingService {
    private List<Movie> movies;
    private List<Screen> screens;
    private Map<Integer, ShowSchedule> shows;
    private Map<Integer, Booking> bookings;
    private List<Cancellation> cancellations;

    public BookingServiceImpl() {
        this.movies = new ArrayList<>();
        this.screens = new ArrayList<>();
        this.shows = new LinkedHashMap<>();
        this.bookings = new LinkedHashMap<>();
        this.cancellations = new ArrayList<>();
    }

    @Override
    public void addMovie(Movie movie) {
        if (movie == null) throw new IllegalArgumentException("Movie cannot be null");
        movies.add(movie);
    }

    @Override
    public void addScreen(Screen screen) {
        if (screen == null) throw new IllegalArgumentException("Screen cannot be null");
        screens.add(screen);
    }

    @Override
    public void scheduleShow(ShowSchedule show) {
        if (show == null) throw new IllegalArgumentException("Show cannot be null");
        shows.put(show.getShowId(), show);
    }

    @Override
    public List<Movie> getAllMovies() {
        return Collections.unmodifiableList(movies);
    }

    @Override
    public List<Screen> getAllScreens() {
        return Collections.unmodifiableList(screens);
    }

    @Override
    public List<ShowSchedule> getAllShows() {
        return new ArrayList<>(shows.values());
    }

    @Override
    public ShowSchedule getShowById(int showId) throws ShowNotFoundException {
        ShowSchedule show = shows.get(showId);
        if (show == null) {
            throw new ShowNotFoundException("Show with ID " + showId + " does not exist.");
        }
        return show;
    }

    @Override
    public Booking bookTicket(int showId, Customer customer, int numberOfTickets)
            throws ShowNotFoundException, SeatUnavailableException, InvalidBookingException {

        if (numberOfTickets <= 0) {
            throw new InvalidBookingException("Ticket count must be at least 1. Provided: " + numberOfTickets);
        }

        ShowSchedule show = getShowById(showId);

        // Thread-safe reservation
        boolean reserved = show.reserveSeats(numberOfTickets);
        if (!reserved) {
            throw new SeatUnavailableException(
                    String.format("Booking Failed: Requested %d seats, but only %d seats are available for '%s'!",
                            numberOfTickets, show.getAvailableSeats(), show.getMovie().getTitle()),
                    numberOfTickets,
                    show.getAvailableSeats()
            );
        }

        Booking booking = new Booking(customer, show, numberOfTickets);
        bookings.put(booking.getBookingId(), booking);
        return booking;
    }

    @Override
    public Cancellation cancelBooking(int bookingId, double leadTimeHours) throws InvalidBookingException {
        Booking booking = bookings.get(bookingId);
        if (booking == null) {
            throw new InvalidBookingException("Booking ID " + bookingId + " not found!");
        }

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new InvalidBookingException("Booking #" + bookingId + " has already been cancelled!");
        }

        // Apply Business Rules for Refund based on lead time (FR-07)
        double refundPercentage;
        if (leadTimeHours >= 24.0) {
            refundPercentage = 90.0; // 90% refund, 10% cancellation charge
        } else if (leadTimeHours >= 2.0) {
            refundPercentage = 70.0; // 70% refund, 30% charge
        } else {
            refundPercentage = 0.0;  // Non-refundable within 2 hours
        }

        // Update show seats
        booking.getShow().releaseSeats(booking.getNumberOfTickets());
        booking.setStatus(BookingStatus.CANCELLED);

        Cancellation cancellation = new Cancellation(booking, refundPercentage);
        cancellations.add(cancellation);
        return cancellation;
    }

    @Override
    public Booking getBookingById(int bookingId) throws InvalidBookingException {
        Booking booking = bookings.get(bookingId);
        if (booking == null) {
            throw new InvalidBookingException("Booking ID " + bookingId + " not found.");
        }
        return booking;
    }

    @Override
    public List<Booking> getAllBookings() {
        return new ArrayList<>(bookings.values());
    }

    public List<Cancellation> getAllCancellations() {
        return Collections.unmodifiableList(cancellations);
    }
}
