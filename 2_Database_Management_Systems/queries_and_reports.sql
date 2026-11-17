-- =============================================================================
-- SQL QUERIES & OPERATIONAL REPORTS SCRIPT
-- Academic Year: 2026-2027 (ODD) | Velalar College of Engineering & Technology
-- Course: 25ITT31 - Database Management Systems (Unit 2 Alignment)
-- Scenario 30: Movie Theatre Ticket Booking and Box Office Analysis System
-- =============================================================================

USE movie_theatre_db;

-- -----------------------------------------------------------------------------
-- QUERY 1: Basic Projection & Selection (Display all active Sci-Fi Movies)
-- -----------------------------------------------------------------------------
SELECT movie_id, title, genre, language, duration_minutes, release_date
FROM movie
WHERE genre = 'Sci-Fi'
ORDER BY release_date DESC;

-- -----------------------------------------------------------------------------
-- QUERY 2: Multi-Table Inner Join (Active Show Schedules with Details)
-- -----------------------------------------------------------------------------
SELECT 
    s.show_id,
    m.title AS movie_title,
    m.language,
    sc.screen_name,
    s.show_date,
    s.show_time,
    s.ticket_price,
    s.available_seats
FROM show_schedule s
INNER JOIN movie m ON s.movie_id = m.movie_id
INNER JOIN screen sc ON s.screen_id = sc.screen_id
ORDER BY s.show_date, s.show_time;

-- -----------------------------------------------------------------------------
-- QUERY 3 (FR-09): Show-wise Bookings, Available Seats & Occupancy Percentage
-- -----------------------------------------------------------------------------
SELECT 
    s.show_id,
    m.title AS movie_title,
    sc.screen_name,
    sc.seating_capacity,
    s.available_seats,
    (sc.seating_capacity - s.available_seats) AS total_seats_booked,
    ROUND(((sc.seating_capacity - s.available_seats) / sc.seating_capacity) * 100, 2) AS occupancy_rate_pct
FROM show_schedule s
JOIN movie m ON s.movie_id = m.movie_id
JOIN screen sc ON s.screen_id = sc.screen_id
ORDER BY occupancy_rate_pct DESC;

-- -----------------------------------------------------------------------------
-- QUERY 4 (FR-08): Movie-wise Revenue Breakdown (Gross, Refunds, and Net Revenue)
-- -----------------------------------------------------------------------------
SELECT 
    m.title AS movie_title,
    m.genre,
    COUNT(DISTINCT b.booking_id) AS total_bookings_placed,
    COALESCE(SUM(CASE WHEN b.booking_status = 'CONFIRMED' THEN b.number_of_tickets ELSE 0 END), 0) AS confirmed_tickets,
    COALESCE(SUM(b.total_amount), 0) AS gross_revenue,
    COALESCE(SUM(c.refund_amount), 0) AS total_refunded,
    (COALESCE(SUM(b.total_amount), 0) - COALESCE(SUM(c.refund_amount), 0)) AS net_box_office_revenue
FROM movie m
LEFT JOIN show_schedule s ON m.movie_id = s.movie_id
LEFT JOIN booking b ON s.show_id = b.show_id
LEFT JOIN cancellation c ON b.booking_id = c.booking_id
GROUP BY m.movie_id, m.title, m.genre
ORDER BY net_box_office_revenue DESC;

-- -----------------------------------------------------------------------------
-- QUERY 5: Aggregate Functions (Overall Multiplex Performance Metrics)
-- -----------------------------------------------------------------------------
SELECT 
    COUNT(DISTINCT b.booking_id) AS total_bookings,
    SUM(b.number_of_tickets) AS total_tickets_sold,
    SUM(b.total_amount) AS total_gross_collection,
    ROUND(AVG(b.total_amount), 2) AS average_booking_value,
    MAX(b.total_amount) AS highest_single_booking,
    MIN(b.total_amount) AS lowest_single_booking
FROM booking b;

-- -----------------------------------------------------------------------------
-- QUERY 6: Nested Subquery (Shows with Occupancy Above Multiplex Average)
-- -----------------------------------------------------------------------------
SELECT 
    s.show_id,
    m.title,
    sc.screen_name,
    (sc.seating_capacity - s.available_seats) AS booked_seats,
    ROUND(((sc.seating_capacity - s.available_seats) / sc.seating_capacity) * 100, 2) AS occupancy_pct
FROM show_schedule s
JOIN movie m ON s.movie_id = m.movie_id
JOIN screen sc ON s.screen_id = sc.screen_id
WHERE ((sc.seating_capacity - s.available_seats) / sc.seating_capacity) * 100 > (
    SELECT AVG(((sc2.seating_capacity - s2.available_seats) / sc2.seating_capacity) * 100)
    FROM show_schedule s2
    JOIN screen sc2 ON s2.screen_id = sc2.screen_id
)
ORDER BY occupancy_pct DESC;

-- -----------------------------------------------------------------------------
-- QUERY 7: Correlated Subquery (Identify Customers who placed highest-value bookings)
-- -----------------------------------------------------------------------------
SELECT 
    c.customer_id,
    c.customer_name,
    c.phone,
    b.booking_id,
    b.total_amount
FROM customer c
JOIN booking b ON c.customer_id = b.customer_id
WHERE b.total_amount >= (
    SELECT AVG(total_amount) FROM booking
)
ORDER BY b.total_amount DESC;

-- -----------------------------------------------------------------------------
-- QUERY 8 (FR-07): Cancellations and Financial Impact Audit
-- -----------------------------------------------------------------------------
SELECT 
    can.cancellation_id,
    b.booking_id,
    c.customer_name,
    m.title AS movie_title,
    b.total_amount AS original_charge,
    can.cancellation_date,
    can.refund_amount,
    can.cancellation_fee,
    ROUND((can.refund_amount / b.total_amount) * 100, 1) AS refund_percentage
FROM cancellation can
JOIN booking b ON can.booking_id = b.booking_id
JOIN customer c ON b.customer_id = c.customer_id
JOIN show_schedule s ON b.show_id = s.show_id
JOIN movie m ON s.movie_id = m.movie_id;

-- -----------------------------------------------------------------------------
-- QUERY 9: Querying the Materialized View for Operational Reporting
-- -----------------------------------------------------------------------------
SELECT * FROM vw_show_occupancy_report
WHERE occupancy_percentage > 0
ORDER BY occupancy_percentage DESC;

-- -----------------------------------------------------------------------------
-- QUERY 10: Querying the Movie Revenue Summary View
-- -----------------------------------------------------------------------------
SELECT * FROM vw_movie_revenue_summary
ORDER BY net_revenue DESC;
