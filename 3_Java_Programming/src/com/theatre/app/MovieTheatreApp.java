package com.theatre.app;

import com.theatre.model.*;
import com.theatre.service.*;
import com.theatre.exception.*;
import com.theatre.concurrency.ConcurrentBookingSimulator;

import java.util.Scanner;

/**
 * Main Executable Class for Movie Theatre Ticket Booking System.
 * Academic Year: 2026-2027 (ODD) | Class: 25IT3A & 25IT3B
 * Scenario 30: Movie Theatre Ticket Booking and Box Office Analysis System
 */
public class MovieTheatreApp {
    private static BookingServiceImpl bookingService = new BookingServiceImpl();
    private static ReportServiceImpl reportService = new ReportServiceImpl(bookingService);
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        initializeSampleData();

        System.out.println("================================================================================");
        System.out.println("   VELALAR COLLEGE OF ENGINEERING AND TECHNOLOGY (AUTONOMOUS), ERODE - 12      ");
        System.out.println("          DEPARTMENT OF INFORMATION TECHNOLOGY - ACADEMIC YEAR 2026-27          ");
        System.out.println("   MOVIE THEATRE TICKET BOOKING & BOX OFFICE ANALYSIS SYSTEM (SCENARIO 30)      ");
        System.out.println("================================================================================");

        boolean running = true;
        while (running) {
            printMainMenu();
            System.out.print("Enter your option (1-10): ");
            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    displayAllMovies();
                    break;
                case "2":
                    addNewMovie();
                    break;
                case "3":
                    displayAllScreens();
                    break;
                case "4":
                    displayShowSchedules();
                    break;
                case "5":
                    scheduleNewShow();
                    break;
                case "6":
                    bookTicketFlow();
                    break;
                case "7":
                    cancelBookingFlow();
                    break;
                case "8":
                    reportService.displayShowWiseOccupancyReport();
                    break;
                case "9":
                    reportService.displayMovieWiseRevenueReport();
                    break;
                case "10":
                    runConcurrentSimulation();
                    break;
                case "0":
                    System.out.println("\nThank you for using Movie Theatre Management System. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice! Please enter a valid number (0-10).");
            }
        }
    }

    private static void printMainMenu() {
        System.out.println("\n----------------------------- MAIN MENU -----------------------------");
        System.out.println(" 1. Browse All Movies (FR-01)");
        System.out.println(" 2. Add New Movie (FR-01)");
        System.out.println(" 3. View Theatre Screens (FR-02)");
        System.out.println(" 4. View Show Schedules & Seat Availability (FR-03, FR-04)");
        System.out.println(" 5. Schedule a New Movie Show (FR-03)");
        System.out.println(" 6. Book Tickets (FR-05, FR-06)");
        System.out.println(" 7. Cancel Booking & Claim Refund (FR-07)");
        System.out.println(" 8. View Show Occupancy Report (FR-09)");
        System.out.println(" 9. View Movie Revenue & Box Office Performance (FR-08)");
        System.out.println(" 10. Run Multithreaded Concurrency Simulation (Unit 3)");
        System.out.println(" 0. Exit System");
        System.out.println("---------------------------------------------------------------------");
    }

    private static void initializeSampleData() {
        // Initialize Movies
        Movie m1 = new Movie(101, "Avatar: The Way of Water", "Sci-Fi", "English", 192, "2026-10-15");
        Movie m2 = new Movie(102, "Jailer", "Action", "Tamil", 168, "2026-10-20");
        Movie m3 = new Movie(103, "Oppenheimer", "Drama", "English", 180, "2026-10-25");
        Movie m4 = new Movie(104, "Leo", "Action", "Tamil", 164, "2026-11-01");

        bookingService.addMovie(m1);
        bookingService.addMovie(m2);
        bookingService.addMovie(m3);
        bookingService.addMovie(m4);

        // Initialize Screens
        Screen sc1 = new Screen(1, "Audi 1 - IMAX Laser", 100);
        Screen sc2 = new Screen(2, "Audi 2 - Dolby Atmos", 80);
        Screen sc3 = new Screen(3, "Audi 3 - Standard Digital", 60);

        bookingService.addScreen(sc1);
        bookingService.addScreen(sc2);
        bookingService.addScreen(sc3);

        // Initialize Shows
        ShowSchedule s1 = new ShowSchedule(501, m1, sc1, "2026-11-16", "10:00 AM", 250.00);
        ShowSchedule s2 = new ShowSchedule(502, m1, sc1, "2026-11-16", "02:30 PM", 250.00);
        ShowSchedule s3 = new ShowSchedule(503, m2, sc2, "2026-11-16", "11:00 AM", 180.00);
        ShowSchedule s4 = new ShowSchedule(504, m3, sc3, "2026-11-16", "03:00 PM", 150.00);
        ShowSchedule s5 = new ShowSchedule(505, m4, sc2, "2026-11-16", "06:30 PM", 220.00);

        bookingService.scheduleShow(s1);
        bookingService.scheduleShow(s2);
        bookingService.scheduleShow(s3);
        bookingService.scheduleShow(s4);
        bookingService.scheduleShow(s5);

        // Seed some initial confirmed bookings
        try {
            Customer c1 = new Customer(1, "Vaishnavi V", "9876543210", "vaishnavi@vcet.ac.in");
            Customer c2 = new Customer(2, "Karthik S", "9876543211", "karthik@vcet.ac.in");
            Customer c3 = new Customer(3, "Dharani R", "9876543212", "dharani@vcet.ac.in");

            bookingService.bookTicket(501, c1, 4);
            bookingService.bookTicket(501, c2, 2);
            bookingService.bookTicket(503, c3, 5);
        } catch (Exception ignored) {}
    }

    private static void displayAllMovies() {
        System.out.println("\n--- CURRENT MOVIE CATALOG ---");
        bookingService.getAllMovies().forEach(System.out::println);
    }

    private static void addNewMovie() {
        try {
            System.out.println("\n--- ADD NEW MOVIE ---");
            System.out.print("Enter Movie ID: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Enter Title: ");
            String title = scanner.nextLine().trim();
            System.out.print("Enter Genre: ");
            String genre = scanner.nextLine().trim();
            System.out.print("Enter Language: ");
            String lang = scanner.nextLine().trim();
            System.out.print("Enter Duration (minutes): ");
            int duration = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Enter Release Date (YYYY-MM-DD): ");
            String releaseDate = scanner.nextLine().trim();

            Movie m = new Movie(id, title, genre, lang, duration, releaseDate);
            bookingService.addMovie(m);
            System.out.println("Movie added successfully: " + m.getTitle());
        } catch (NumberFormatException e) {
            System.err.println("Input format error: Please enter valid numbers for ID and Duration.");
        } catch (Exception e) {
            System.err.println("Error adding movie: " + e.getMessage());
        }
    }

    private static void displayAllScreens() {
        System.out.println("\n--- THEATRE SCREENS ---");
        bookingService.getAllScreens().forEach(System.out::println);
    }

    private static void displayShowSchedules() {
        System.out.println("\n--- SCHEDULED SHOWS & LIVE SEAT AVAILABILITY ---");
        bookingService.getAllShows().forEach(System.out::println);
    }

    private static void scheduleNewShow() {
        try {
            System.out.println("\n--- SCHEDULE A NEW SHOW ---");
            displayAllMovies();
            System.out.print("Enter Movie ID: ");
            int movieId = Integer.parseInt(scanner.nextLine().trim());
            Movie movie = bookingService.getAllMovies().stream()
                    .filter(m -> m.getMovieId() == movieId)
                    .findFirst()
                    .orElseThrow(() -> new ShowNotFoundException("Movie ID " + movieId + " does not exist."));

            displayAllScreens();
            System.out.print("Enter Screen ID: ");
            int screenId = Integer.parseInt(scanner.nextLine().trim());
            Screen screen = bookingService.getAllScreens().stream()
                    .filter(sc -> sc.getScreenId() == screenId)
                    .findFirst()
                    .orElseThrow(() -> new ShowNotFoundException("Screen ID " + screenId + " does not exist."));

            System.out.print("Enter Show ID: ");
            int showId = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Enter Show Date (YYYY-MM-DD): ");
            String date = scanner.nextLine().trim();
            System.out.print("Enter Show Time (e.g. 06:00 PM): ");
            String time = scanner.nextLine().trim();
            System.out.print("Enter Ticket Price (₹): ");
            double price = Double.parseDouble(scanner.nextLine().trim());

            ShowSchedule show = new ShowSchedule(showId, movie, screen, date, time, price);
            bookingService.scheduleShow(show);
            System.out.println("Show scheduled successfully: Show #" + showId);
        } catch (Exception e) {
            System.err.println("Failed to schedule show: " + e.getMessage());
        }
    }

    private static void bookTicketFlow() {
        try {
            System.out.println("\n--- BOOK MOVIE TICKETS ---");
            displayShowSchedules();
            System.out.print("Enter Show ID to book: ");
            int showId = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Enter Customer Name: ");
            String name = scanner.nextLine().trim();
            System.out.print("Enter Customer Phone: ");
            String phone = scanner.nextLine().trim();
            System.out.print("Enter Customer Email: ");
            String email = scanner.nextLine().trim();
            Customer customer = new Customer((int)(Math.random()*1000) + 10, name, phone, email);

            System.out.print("Enter Number of Tickets: ");
            int tickets = Integer.parseInt(scanner.nextLine().trim());

            // Process booking (Capacity validation enforced in service)
            Booking booking = bookingService.bookTicket(showId, customer, tickets);

            // Print generated receipt
            System.out.println(booking.generateReceipt());

        } catch (SeatUnavailableException e) {
            System.err.println("\n[OVERBOOKING PREVENTED] " + e.getMessage());
            System.err.printf("Available Seats: %d | Requested: %d\n", e.getAvailableSeats(), e.getRequestedSeats());
        } catch (InvalidBookingException | ShowNotFoundException e) {
            System.err.println("\n[BOOKING ERROR] " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("\n[INPUT ERROR] Invalid numeric input entered.");
        }
    }

    private static void cancelBookingFlow() {
        try {
            System.out.println("\n--- CANCEL BOOKING & CLAIM REFUND ---");
            System.out.print("Enter Booking ID: ");
            int bookingId = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Enter Lead Time in Hours before Show Start (e.g. 48, 5, 1): ");
            double leadTime = Double.parseDouble(scanner.nextLine().trim());

            Cancellation cancellation = bookingService.cancelBooking(bookingId, leadTime);
            System.out.println(cancellation.generateCancellationSlip());

        } catch (InvalidBookingException e) {
            System.err.println("\n[CANCELLATION ERROR] " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("\n[INPUT ERROR] Please enter valid numbers.");
        }
    }

    private static void runConcurrentSimulation() {
        ConcurrentBookingSimulator.runSimulation(bookingService, 501);
    }
}
