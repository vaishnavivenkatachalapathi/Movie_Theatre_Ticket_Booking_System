package com.theatre.model;

/**
 * ShowSchedule domain entity (FR-03, FR-04: Schedule movie shows & track seat availability).
 * Incorporates Thread Synchronization (Unit 3) to prevent double-booking race conditions.
 */
public class ShowSchedule {
    private int showId;
    private Movie movie;
    private Screen screen;
    private String showDate;
    private String showTime;
    private double ticketPrice;
    private int availableSeats;
    private int bookedSeats;

    public ShowSchedule(int showId, Movie movie, Screen screen, String showDate, String showTime, double ticketPrice) {
        this.showId = showId;
        this.movie = movie;
        this.screen = screen;
        this.showDate = showDate;
        this.showTime = showTime;
        this.ticketPrice = ticketPrice;
        this.availableSeats = screen.getSeatingCapacity();
        this.bookedSeats = 0;
    }

    public int getShowId() { return showId; }
    public Movie getMovie() { return movie; }
    public Screen getScreen() { return screen; }
    public String getShowDate() { return showDate; }
    public String getShowTime() { return showTime; }
    public double getTicketPrice() { return ticketPrice; }
    public int getAvailableSeats() { return availableSeats; }
    public int getBookedSeats() { return bookedSeats; }

    /**
     * Thread-Safe Seat Reservation using synchronized method (Unit 3).
     * Prevents overbooking race conditions when multiple threads book simultaneously.
     */
    public synchronized boolean reserveSeats(int count) {
        if (count <= 0) return false;
        if (availableSeats >= count) {
            availableSeats -= count;
            bookedSeats += count;
            return true;
        }
        return false;
    }

    /**
     * Thread-Safe Seat Release upon cancellation.
     */
    public synchronized void releaseSeats(int count) {
        if (count <= 0) return;
        availableSeats = Math.min(screen.getSeatingCapacity(), availableSeats + count);
        bookedSeats = Math.max(0, bookedSeats - count);
    }

    /**
     * Calculate current theatre occupancy percentage (FR-09).
     */
    public double getOccupancyPercentage() {
        int capacity = screen.getSeatingCapacity();
        if (capacity == 0) return 0.0;
        return ((double) bookedSeats / capacity) * 100.0;
    }

    @Override
    public String toString() {
        return String.format("Show #%d: '%s' | Screen: %s | %s %s | Price: ₹%.2f | Available: %d/%d (Occupancy: %.1f%%)",
                showId, movie.getTitle(), screen.getScreenName(), showDate, showTime,
                ticketPrice, availableSeats, screen.getSeatingCapacity(), getOccupancyPercentage());
    }
}
