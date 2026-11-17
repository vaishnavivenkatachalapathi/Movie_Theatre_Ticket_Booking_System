package com.theatre.service;

import com.theatre.model.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Report service implementation heavily leveraging Java 8+ Stream API,
 * Lambdas, Method References, and Collectors (Unit 5 Alignment).
 */
public class ReportServiceImpl implements IReportService {
    private BookingServiceImpl bookingService;

    public ReportServiceImpl(BookingServiceImpl bookingService) {
        this.bookingService = bookingService;
    }

    @Override
    public void displayShowWiseOccupancyReport() {
        List<ShowSchedule> shows = bookingService.getAllShows();

        System.out.println("\n==========================================================================================");
        System.out.println("                 SHOW-WISE BOOKINGS & OCCUPANCY PERFORMANCE REPORT (FR-09)                ");
        System.out.println("==========================================================================================");
        System.out.printf("%-8s | %-25s | %-16s | %-8s | %-10s | %-10s | %-10s\n",
                "Show ID", "Movie Title", "Screen", "Capacity", "Booked", "Available", "Occupancy %");
        System.out.println("------------------------------------------------------------------------------------------");

        shows.stream()
                .sorted(Comparator.comparingDouble(ShowSchedule::getOccupancyPercentage).reversed())
                .forEach(s -> System.out.printf("%-8d | %-25s | %-16s | %-8d | %-10d | %-10d | %9.1f%%\n",
                        s.getShowId(),
                        s.getMovie().getTitle().length() > 25 ? s.getMovie().getTitle().substring(0, 22) + "..." : s.getMovie().getTitle(),
                        s.getScreen().getScreenName(),
                        s.getScreen().getSeatingCapacity(),
                        s.getBookedSeats(),
                        s.getAvailableSeats(),
                        s.getOccupancyPercentage()));

        System.out.println("==========================================================================================");
        System.out.printf(" Multiplex Average Occupancy: %.2f%%\n", calculateOverallMultiplexOccupancy());
        System.out.println("==========================================================================================\n");
    }

    @Override
    public void displayMovieWiseRevenueReport() {
        List<Booking> bookings = bookingService.getAllBookings();
        List<Cancellation> cancellations = bookingService.getAllCancellations();

        // Calculate refunds per booking ID using Streams
        Map<Integer, Double> refundMap = cancellations.stream()
                .collect(Collectors.toMap(c -> c.getBooking().getBookingId(), Cancellation::getRefundAmount, (a, b) -> a));

        // Group gross revenue by Movie Title using Stream API Collectors.groupingBy & summingDouble
        Map<String, Double> grossRevenueByMovie = bookings.stream()
                .collect(Collectors.groupingBy(
                        b -> b.getShow().getMovie().getTitle(),
                        Collectors.summingDouble(Booking::getTotalAmount)
                ));

        // Group total refunds by Movie Title
        Map<String, Double> refundsByMovie = cancellations.stream()
                .collect(Collectors.groupingBy(
                        c -> c.getBooking().getShow().getMovie().getTitle(),
                        Collectors.summingDouble(Cancellation::getRefundAmount)
                ));

        System.out.println("\n==========================================================================================");
        System.out.println("                 MOVIE-WISE BOX OFFICE REVENUE ANALYSIS (FR-08)                           ");
        System.out.println("==========================================================================================");
        System.out.printf("%-30s | %-15s | %-15s | %-15s\n", "Movie Title", "Gross Revenue", "Total Refunds", "Net Revenue");
        System.out.println("------------------------------------------------------------------------------------------");

        grossRevenueByMovie.forEach((movie, gross) -> {
            double refunds = refundsByMovie.getOrDefault(movie, 0.0);
            double net = gross - refunds;
            System.out.printf("%-30s | ₹%13.2f | ₹%13.2f | ₹%13.2f\n",
                    movie.length() > 30 ? movie.substring(0, 27) + "..." : movie,
                    gross, refunds, net);
        });

        System.out.println("==========================================================================================");
        System.out.printf(" Multiplex Total Net Collection: ₹%.2f\n", calculateOverallMultiplexRevenue());
        System.out.println("==========================================================================================\n");
    }

    @Override
    public Map<String, Double> calculateMovieWiseRevenue() {
        return bookingService.getAllBookings().stream()
                .filter(b -> b.getStatus() == BookingStatus.CONFIRMED)
                .collect(Collectors.groupingBy(
                        b -> b.getShow().getMovie().getTitle(),
                        Collectors.summingDouble(Booking::getTotalAmount)
                ));
    }

    @Override
    public double calculateOverallMultiplexRevenue() {
        double gross = bookingService.getAllBookings().stream()
                .mapToDouble(Booking::getTotalAmount)
                .sum();

        double refunds = bookingService.getAllCancellations().stream()
                .mapToDouble(Cancellation::getRefundAmount)
                .sum();

        return gross - refunds;
    }

    @Override
    public double calculateOverallMultiplexOccupancy() {
        List<ShowSchedule> shows = bookingService.getAllShows();
        int totalCap = shows.stream().mapToInt(s -> s.getScreen().getSeatingCapacity()).sum();
        int totalBooked = shows.stream().mapToInt(ShowSchedule::getBookedSeats).sum();
        return totalCap == 0 ? 0.0 : ((double) totalBooked / totalCap) * 100.0;
    }
}
