package com.theatre.concurrency;

import com.theatre.model.*;
import com.theatre.service.BookingServiceImpl;

/**
 * Multithreaded Concurrency Simulator (Unit 3 Alignment).
 * Simulates simultaneous booking attempts from multiple threads on limited seats
 * demonstrating thread synchronization and race condition prevention.
 */
public class ConcurrentBookingSimulator {

    public static void runSimulation(BookingServiceImpl bookingService, int showId) {
        System.out.println("\n>>> STARTING MULTITHREADED CONCURRENT BOOKING SIMULATION (Unit 3) <<<");
        try {
            ShowSchedule show = bookingService.getShowById(showId);
            System.out.printf("Target Show: %s | Currently Available Seats: %d\n",
                    show.getMovie().getTitle(), show.getAvailableSeats());

            // Create 5 concurrent threads attempting to book 2 seats each simultaneously
            Thread[] bookingThreads = new Thread[5];
            for (int i = 0; i < 5; i++) {
                final int threadNum = i + 1;
                final Customer cust = new Customer(100 + threadNum, "Concurrent_User_" + threadNum, "900000000" + threadNum, "user" + threadNum + "@test.com");
                
                bookingThreads[i] = new Thread(() -> {
                    try {
                        System.out.printf("[Thread %d] Initiating booking request for 2 seats...\n", threadNum);
                        Booking b = bookingService.bookTicket(showId, cust, 2);
                        System.out.printf(">>> [Thread %d] SUCCESS: Booking #%d confirmed! Remaining seats: %d\n",
                                threadNum, b.getBookingId(), show.getAvailableSeats());
                    } catch (Exception ex) {
                        System.out.printf("xxx [Thread %d] REJECTED: %s\n", threadNum, ex.getMessage());
                    }
                }, "Booking-Worker-" + threadNum);
            }

            // Start all threads simultaneously
            for (Thread t : bookingThreads) {
                t.start();
            }

            // Wait for all threads to terminate (join)
            for (Thread t : bookingThreads) {
                t.join();
            }

            System.out.println(">>> MULTITHREADED SIMULATION COMPLETE: Final Available Seats = " + show.getAvailableSeats() + " <<<\n");

        } catch (Exception e) {
            System.err.println("Simulation Error: " + e.getMessage());
        }
    }
}
