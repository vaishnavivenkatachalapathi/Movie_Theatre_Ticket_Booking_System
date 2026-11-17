package com.theatre.dao;

import com.theatre.model.Booking;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Data Access Object (DAO) for persisting bookings into MySQL relational store (Unit 4).
 */
public class BookingDAO {

    public boolean persistBooking(Booking booking) {
        String sql = "INSERT INTO booking (booking_id, customer_id, show_id, number_of_tickets, total_amount, booking_date, booking_status) " +
                     "VALUES (?, ?, ?, ?, ?, NOW(), ?)";

        Connection conn = DBConnection.getConnection();
        if (conn == null) return false;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, booking.getBookingId());
            pstmt.setInt(2, booking.getCustomer().getId());
            pstmt.setInt(3, booking.getShow().getShowId());
            pstmt.setInt(4, booking.getNumberOfTickets());
            pstmt.setDouble(5, booking.getTotalAmount());
            pstmt.setString(6, booking.getStatus().name());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[DAO ERROR] Persist booking failed: " + e.getMessage());
            return false;
        }
    }
}
