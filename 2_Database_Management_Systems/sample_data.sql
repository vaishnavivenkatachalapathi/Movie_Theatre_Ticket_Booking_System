-- =============================================================================
-- SAMPLE DATA SCRIPT: MOVIE THEATRE TICKET BOOKING SYSTEM
-- Academic Year: 2026-2027 (ODD) | Velalar College of Engineering & Technology
-- Scenario 30: Movie Theatre Ticket Booking and Box Office Analysis System
-- =============================================================================

USE movie_theatre_db;

-- -----------------------------------------------------------------------------
-- 1. INSERT MOVIES (FR-01)
-- -----------------------------------------------------------------------------
INSERT INTO movie (movie_id, title, genre, language, duration_minutes, release_date) VALUES
(101, 'Avatar: The Way of Water', 'Sci-Fi', 'English', 192, '2026-10-15'),
(102, 'Jailer', 'Action', 'Tamil', 168, '2026-10-20'),
(103, 'Oppenheimer', 'Drama', 'English', 180, '2026-10-25'),
(104, 'Leo', 'Action', 'Tamil', 164, '2026-11-01'),
(105, 'Interstellar: Re-release', 'Sci-Fi', 'English', 169, '2026-11-05'),
(106, 'Ponniyin Selvan Part 2', 'Historical Drama', 'Tamil', 170, '2026-11-10');

-- -----------------------------------------------------------------------------
-- 2. INSERT SCREENS (FR-02)
-- -----------------------------------------------------------------------------
INSERT INTO screen (screen_id, screen_name, seating_capacity) VALUES
(1, 'Audi 1 - IMAX Laser', 120),
(2, 'Audi 2 - Dolby Atmos 4K', 100),
(3, 'Audi 3 - Standard Digital', 80),
(4, 'Audi 4 - VIP Recliner', 50);

-- -----------------------------------------------------------------------------
-- 3. INSERT SHOW SCHEDULES (FR-03 & FR-04)
-- Note: Available seats initialized to Screen Seating Capacity
-- -----------------------------------------------------------------------------
INSERT INTO show_schedule (show_id, movie_id, screen_id, show_date, show_time, ticket_price, available_seats) VALUES
(501, 101, 1, '2026-11-16', '10:00:00', 250.00, 120),
(502, 101, 1, '2026-11-16', '14:30:00', 250.00, 120),
(503, 101, 1, '2026-11-16', '19:00:00', 300.00, 120),
(504, 102, 2, '2026-11-16', '11:00:00', 180.00, 100),
(505, 102, 2, '2026-11-16', '15:30:00', 180.00, 100),
(506, 102, 2, '2026-11-16', '20:00:00', 220.00, 100),
(507, 103, 3, '2026-11-16', '10:30:00', 150.00, 80),
(508, 103, 3, '2026-11-16', '16:00:00', 150.00, 80),
(509, 104, 4, '2026-11-16', '18:00:00', 350.00, 50),
(510, 105, 1, '2026-11-17', '18:30:00', 280.00, 120);

-- -----------------------------------------------------------------------------
-- 4. INSERT CUSTOMERS
-- -----------------------------------------------------------------------------
INSERT INTO customer (customer_id, customer_name, phone, email) VALUES
(1, 'Vaishnavi V', '9876543210', 'vaishnavi@vcet.ac.in'),
(2, 'Karthik S', '9876543211', 'karthik@vcet.ac.in'),
(3, 'Dharani R', '9876543212', 'dharani@vcet.ac.in'),
(4, 'Sanjay Kumar', '9876543213', 'sanjay@gmail.com'),
(5, 'Priya Mohan', '9876543214', 'priya@yahoo.com'),
(6, 'Anand R', '9876543215', 'anand@outlook.com'),
(7, 'Deepa M', '9876543216', 'deepa@gmail.com'),
(8, 'Vignesh P', '9876543217', 'vignesh@vcet.ac.in');

-- -----------------------------------------------------------------------------
-- 5. INSERT BOOKINGS (FR-05)
-- (Triggers will automatically adjust available_seats in show_schedule)
-- -----------------------------------------------------------------------------
INSERT INTO booking (booking_id, customer_id, show_id, number_of_tickets, total_amount, booking_date, booking_status) VALUES
(1001, 1, 501, 4, 1000.00, '2026-11-15 09:30:00', 'CONFIRMED'),
(1002, 2, 501, 2, 500.00,  '2026-11-15 10:15:00', 'CONFIRMED'),
(1003, 3, 502, 5, 1250.00, '2026-11-15 11:00:00', 'CONFIRMED'),
(1004, 4, 503, 6, 1800.00, '2026-11-15 14:20:00', 'CONFIRMED'),
(1005, 5, 503, 3, 900.00,  '2026-11-15 15:45:00', 'CONFIRMED'),
(1006, 6, 504, 4, 720.00,  '2026-11-15 16:30:00', 'CONFIRMED'),
(1007, 7, 505, 2, 360.00,  '2026-11-15 17:00:00', 'CONFIRMED'),
(1008, 8, 506, 8, 1760.00, '2026-11-15 18:10:00', 'CONFIRMED'),
(1009, 1, 506, 2, 440.00,  '2026-11-15 18:40:00', 'CONFIRMED'),
(1010, 2, 507, 3, 450.00,  '2026-11-15 19:20:00', 'CONFIRMED'),
(1011, 3, 509, 4, 1400.00, '2026-11-15 20:00:00', 'CONFIRMED'),
(1012, 4, 509, 2, 700.00,  '2026-11-15 21:00:00', 'CONFIRMED'),
(1013, 5, 510, 4, 1120.00, '2026-11-16 08:00:00', 'CONFIRMED'),
(1014, 6, 502, 2, 500.00,  '2026-11-16 09:00:00', 'CONFIRMED');

-- -----------------------------------------------------------------------------
-- 6. INSERT PAYMENTS
-- -----------------------------------------------------------------------------
INSERT INTO payment (payment_id, booking_id, payment_mode, amount, payment_status, transaction_time) VALUES
(2001, 1001, 'UPI', 1000.00, 'SUCCESS', '2026-11-15 09:31:00'),
(2002, 1002, 'CREDIT_CARD', 500.00, 'SUCCESS', '2026-11-15 10:16:00'),
(2003, 1003, 'DEBIT_CARD', 1250.00, 'SUCCESS', '2026-11-15 11:01:00'),
(2004, 1004, 'UPI', 1800.00, 'SUCCESS', '2026-11-15 14:21:00'),
(2005, 1005, 'NET_BANKING', 900.00, 'SUCCESS', '2026-11-15 15:46:00'),
(2006, 1006, 'CASH', 720.00, 'SUCCESS', '2026-11-15 16:31:00'),
(2007, 1007, 'UPI', 360.00, 'SUCCESS', '2026-11-15 17:01:00'),
(2008, 1008, 'CREDIT_CARD', 1760.00, 'SUCCESS', '2026-11-15 18:11:00'),
(2009, 1009, 'UPI', 440.00, 'SUCCESS', '2026-11-15 18:41:00'),
(2010, 1010, 'DEBIT_CARD', 450.00, 'SUCCESS', '2026-11-15 19:21:00'),
(2011, 1011, 'UPI', 1400.00, 'SUCCESS', '2026-11-15 20:01:00'),
(2012, 1012, 'CREDIT_CARD', 700.00, 'SUCCESS', '2026-11-15 21:01:00'),
(2013, 1013, 'UPI', 1120.00, 'SUCCESS', '2026-11-16 08:01:00'),
(2014, 1014, 'UPI', 500.00, 'SUCCESS', '2026-11-16 09:01:00');

-- -----------------------------------------------------------------------------
-- 7. INSERT CANCELLATIONS (FR-07)
-- (Customer 6 cancels Booking 1006 (>24h lead time: 90% refund, 10% fee))
-- -----------------------------------------------------------------------------
INSERT INTO cancellation (cancellation_id, booking_id, cancellation_date, refund_amount, cancellation_fee) VALUES
(3001, 1006, '2026-11-15 19:00:00', 648.00, 72.00);

-- Update corresponding payment to REFUNDED
UPDATE payment SET payment_status = 'REFUNDED' WHERE booking_id = 1006;
