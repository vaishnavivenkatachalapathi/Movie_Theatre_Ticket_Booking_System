package com.theatre.service;

import java.util.Map;

/**
 * Interface for generating operational reports and financial calculations (Unit 2, FR-08, FR-09).
 */
public interface IReportService {
    void displayShowWiseOccupancyReport();
    void displayMovieWiseRevenueReport();
    Map<String, Double> calculateMovieWiseRevenue();
    double calculateOverallMultiplexRevenue();
    double calculateOverallMultiplexOccupancy();
}
