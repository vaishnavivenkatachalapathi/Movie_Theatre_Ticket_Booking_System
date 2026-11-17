package com.theatre.test;

import com.theatre.model.*;
import com.theatre.service.BookingServiceImpl;
import com.theatre.exception.*;

/**
 * Automated Unit & Integration Test Suite (Unit 4: Software Testing).
 * Executes automated assertions against core business rules and edge cases.
 */
public class BookingServiceTest {
    private static int totalTests = 0;
    private static int passedTests = 0;

    public static void main(String[] args) {
        System.out.println("================================================================================");
        System.out.println("   AUTOMATED UNIT & INTEGRATION TEST RUNNER (Course: 25ITT33 & 25ITT32)        ");
        System.out.println("   Scenario 30: Movie Theatre Ticket Booking System                             ");
        System.out.println("================================================================================");

        testValidBooking();
        testOverbookingPrevention();
        testInvalidTicketCount();
        testCancellationRefundOver24Hours();
        testCancellationRefundUnder2Hours();
        testDuplicateCancellation();
        testThreadSynchronizationSafety();

        System.out.println("\n--------------------------------------------------------------------------------");
        System.out.printf(" TEST SUMMARY: Total Executed = %d | Passed = %d | Failed = %d | Success Rate = %.1f%%\n",
                totalTests, passedTests, (totalTests - passedTests), ((double) passedTests / totalTests) * 100.0);
        System.out.println("================================================================================");
    }

    private static void assertTrue(String testName, boolean condition, String details) {
        totalTests++;
        if (condition) {
            passedTests++;
            System.out.printf("[PASS] %-40s : %s\n", testName, details);
        } else {
            System.err.printf("[FAIL] %-40s : %s\n", testName, details);
        }
    }

    private static void testValidBooking() {
        BookingServiceImpl service = new BookingServiceImpl();
        Movie m = new Movie(101, "Avatar", "Sci-Fi", "English", 190, "2026-10-10");
        Screen sc = new Screen(1, "Audi 1", 50);
        ShowSchedule show = new ShowSchedule(1, m, sc, "2026-11-20", "18:00", 200.0);
        service.scheduleShow(show);

        Customer c = new Customer(1, "Alice", "9876543210", "alice@test.com");
        try {
            Booking b = service.bookTicket(1, c, 5);
            boolean check = (b != null && b.getNumberOfTickets() == 5 && show.getAvailableSeats() == 45 && b.getTotalAmount() == 1000.0);
            assertTrue("testValidBooking", check, "Seats correctly decremented from 50 to 45; total ₹1000.00");
        } catch (Exception e) {
            assertTrue("testValidBooking", false, "Unexpected exception: " + e.getMessage());
        }
    }

    private static void testOverbookingPrevention() {
        BookingServiceImpl service = new BookingServiceImpl();
        Movie m = new Movie(102, "Leo", "Action", "Tamil", 160, "2026-10-10");
        Screen sc = new Screen(2, "Audi 2", 10);
        ShowSchedule show = new ShowSchedule(2, m, sc, "2026-11-20", "18:00", 150.0);
        service.scheduleShow(show);

        Customer c = new Customer(2, "Bob", "9876543211", "bob@test.com");
        try {
            // Attempt to book 15 seats when only 10 exist
            service.bookTicket(2, c, 15);
            assertTrue("testOverbookingPrevention", false, "Expected SeatUnavailableException but booking succeeded!");
        } catch (SeatUnavailableException e) {
            assertTrue("testOverbookingPrevention", true, "Caught SeatUnavailableException: " + e.getMessage());
        } catch (Exception e) {
            assertTrue("testOverbookingPrevention", false, "Unexpected exception: " + e.getMessage());
        }
    }

    private static void testInvalidTicketCount() {
        BookingServiceImpl service = new BookingServiceImpl();
        Movie m = new Movie(103, "Jailer", "Action", "Tamil", 160, "2026-10-10");
        Screen sc = new Screen(3, "Audi 3", 50);
        ShowSchedule show = new ShowSchedule(3, m, sc, "2026-11-20", "18:00", 150.0);
        service.scheduleShow(show);

        Customer c = new Customer(3, "Charlie", "9876543212", "charlie@test.com");
        try {
            service.bookTicket(3, c, 0); // 0 tickets requested
            assertTrue("testInvalidTicketCount", false, "Expected InvalidBookingException for 0 seats");
        } catch (InvalidBookingException e) {
            assertTrue("testInvalidTicketCount", true, "Caught InvalidBookingException for non-positive ticket input");
        } catch (Exception e) {
            assertTrue("testInvalidTicketCount", false, "Unexpected exception: " + e.getMessage());
        }
    }

    private static void testCancellationRefundOver24Hours() {
        BookingServiceImpl service = new BookingServiceImpl();
        Movie m = new Movie(104, "Oppenheimer", "Drama", "English", 180, "2026-10-10");
        Screen sc = new Screen(4, "Audi 4", 50);
        ShowSchedule show = new ShowSchedule(4, m, sc, "2026-11-20", "18:00", 200.0);
        service.scheduleShow(show);

        Customer c = new Customer(4, "Diana", "9876543213", "diana@test.com");
        try {
            Booking b = service.bookTicket(4, c, 4); // ₹800.00 total
            Cancellation can = service.cancelBooking(b.getBookingId(), 48.0); // 48 hrs lead time -> 90% refund
            boolean check = (can.getRefundAmount() == 720.0 && can.getCancellationFee() == 80.0 && show.getAvailableSeats() == 50);
            assertTrue("testCancellationOver24Hours", check, "90% refund disbursed (₹720) & seats restored to 50");
        } catch (Exception e) {
            assertTrue("testCancellationOver24Hours", false, "Exception: " + e.getMessage());
        }
    }

    private static void testCancellationRefundUnder2Hours() {
        BookingServiceImpl service = new BookingServiceImpl();
        Movie m = new Movie(105, "Interstellar", "Sci-Fi", "English", 170, "2026-10-10");
        Screen sc = new Screen(5, "Audi 5", 50);
        ShowSchedule show = new ShowSchedule(5, m, sc, "2026-11-20", "18:00", 250.0);
        service.scheduleShow(show);

        Customer c = new Customer(5, "Evan", "9876543214", "evan@test.com");
        try {
            Booking b = service.bookTicket(5, c, 2); // ₹500.00
            Cancellation can = service.cancelBooking(b.getBookingId(), 1.0); // 1 hr lead time -> 0% refund
            boolean check = (can.getRefundAmount() == 0.0 && can.getCancellationFee() == 500.0 && show.getAvailableSeats() == 50);
            assertTrue("testCancellationUnder2Hours", check, "Non-refundable window applied (₹0 refund); seats restored");
        } catch (Exception e) {
            assertTrue("testCancellationUnder2Hours", false, "Exception: " + e.getMessage());
        }
    }

    private static void testDuplicateCancellation() {
        BookingServiceImpl service = new BookingServiceImpl();
        Movie m = new Movie(106, "PS-2", "Drama", "Tamil", 170, "2026-10-10");
        Screen sc = new Screen(6, "Audi 6", 50);
        ShowSchedule show = new ShowSchedule(6, m, sc, "2026-11-20", "18:00", 150.0);
        service.scheduleShow(show);

        Customer c = new Customer(6, "Fiona", "9876543215", "fiona@test.com");
        try {
            Booking b = service.bookTicket(6, c, 2);
            service.cancelBooking(b.getBookingId(), 30.0);
            // Attempt second cancellation on the same booking
            service.cancelBooking(b.getBookingId(), 30.0);
            assertTrue("testDuplicateCancellation", false, "Expected InvalidBookingException on duplicate cancel");
        } catch (InvalidBookingException e) {
            assertTrue("testDuplicateCancellation", true, "Caught InvalidBookingException: " + e.getMessage());
        } catch (Exception e) {
            assertTrue("testDuplicateCancellation", false, "Exception: " + e.getMessage());
        }
    }

    private static void testThreadSynchronizationSafety() {
        BookingServiceImpl service = new BookingServiceImpl();
        Movie m = new Movie(107, "Avatar 3", "Sci-Fi", "English", 190, "2026-10-10");
        Screen sc = new Screen(7, "Audi 7", 10);
        ShowSchedule show = new ShowSchedule(7, m, sc, "2026-11-20", "18:00", 200.0);
        service.scheduleShow(show);

        // 5 threads attempting to book 3 seats each = 15 seats requested, but only 10 available
        int threadsCount = 5;
        Thread[] threads = new Thread[threadsCount];
        int[] successCount = new int[1];

        for (int i = 0; i < threadsCount; i++) {
            final int id = i + 1;
            threads[i] = new Thread(() -> {
                Customer cust = new Customer(id, "User" + id, "987000000" + id, "u" + id + "@test.com");
                try {
                    service.bookTicket(7, cust, 3);
                    synchronized (successCount) { successCount[0]++; }
                } catch (Exception ignored) {}
            });
        }

        for (Thread t : threads) t.start();
        for (Thread t : threads) {
            try { t.join(); } catch (InterruptedException ignored) {}
        }

        // With 10 seats and 3 per booking, exactly 3 bookings should succeed (9 seats total, 1 seat left)
        boolean check = (successCount[0] == 3 && show.getAvailableSeats() == 1 && show.getBookedSeats() == 9);
        assertTrue("testThreadSynchronizationSafety", check, "Synchronized lock prevented overbooking: 3 succeeded, 1 seat left");
    }
}
