-- =============================================================================
-- DATABASE SCHEMA: MOVIE THEATRE TICKET BOOKING & BOX OFFICE ANALYSIS SYSTEM
-- Academic Year: 2026-2027 (ODD) | Velalar College of Engineering & Technology
-- Course: 25ITT31 - Database Management Systems
-- Scenario 30: Movie Theatre Ticket Booking and Box Office Analysis System
-- =============================================================================

DROP DATABASE IF EXISTS movie_theatre_db;
CREATE DATABASE movie_theatre_db;
USE movie_theatre_db;

-- -----------------------------------------------------------------------------
-- 1. TABLE: MOVIE (FR-01: Manage Movie Details)
-- -----------------------------------------------------------------------------
CREATE TABLE movie (
    movie_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    genre VARCHAR(50) NOT NULL,
    language VARCHAR(30) NOT NULL,
    duration_minutes INT NOT NULL,
    release_date DATE NOT NULL,
    CONSTRAINT chk_movie_duration CHECK (duration_minutes > 0)
) ENGINE=InnoDB;

-- -----------------------------------------------------------------------------
-- 2. TABLE: SCREEN (FR-02: Manage Theatre Screens)
-- -----------------------------------------------------------------------------
CREATE TABLE screen (
    screen_id INT AUTO_INCREMENT PRIMARY KEY,
    screen_name VARCHAR(50) NOT NULL UNIQUE,
    seating_capacity INT NOT NULL,
    CONSTRAINT chk_screen_capacity CHECK (seating_capacity > 0)
) ENGINE=InnoDB;

-- -----------------------------------------------------------------------------
-- 3. TABLE: SHOW_SCHEDULE (FR-03: Schedule Movie Shows, FR-04: Seat Availability)
-- -----------------------------------------------------------------------------
CREATE TABLE show_schedule (
    show_id INT AUTO_INCREMENT PRIMARY KEY,
    movie_id INT NOT NULL,
    screen_id INT NOT NULL,
    show_date DATE NOT NULL,
    show_time TIME NOT NULL,
    ticket_price DECIMAL(10, 2) NOT NULL,
    available_seats INT NOT NULL,
    CONSTRAINT fk_show_movie FOREIGN KEY (movie_id) REFERENCES movie(movie_id) ON DELETE RESTRICT,
    CONSTRAINT fk_show_screen FOREIGN KEY (screen_id) REFERENCES screen(screen_id) ON DELETE RESTRICT,
    CONSTRAINT chk_ticket_price CHECK (ticket_price >= 0.00),
    CONSTRAINT chk_available_seats CHECK (available_seats >= 0),
    CONSTRAINT uq_screen_datetime UNIQUE (screen_id, show_date, show_time)
) ENGINE=InnoDB;

-- -----------------------------------------------------------------------------
-- 4. TABLE: CUSTOMER (Customer Entity)
-- -----------------------------------------------------------------------------
CREATE TABLE customer (
    customer_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_name VARCHAR(100) NOT NULL,
    phone VARCHAR(15) NOT NULL UNIQUE,
    email VARCHAR(100)
) ENGINE=InnoDB;

-- -----------------------------------------------------------------------------
-- 5. TABLE: BOOKING (FR-05: Book Tickets, FR-06: Validate Bookings)
-- -----------------------------------------------------------------------------
CREATE TABLE booking (
    booking_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT NOT NULL,
    show_id INT NOT NULL,
    number_of_tickets INT NOT NULL,
    total_amount DECIMAL(10, 2) NOT NULL,
    booking_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    booking_status ENUM('CONFIRMED', 'CANCELLED') NOT NULL DEFAULT 'CONFIRMED',
    CONSTRAINT fk_booking_customer FOREIGN KEY (customer_id) REFERENCES customer(customer_id) ON DELETE CASCADE,
    CONSTRAINT fk_booking_show FOREIGN KEY (show_id) REFERENCES show_schedule(show_id) ON DELETE RESTRICT,
    CONSTRAINT chk_tickets_positive CHECK (number_of_tickets > 0),
    CONSTRAINT chk_total_positive CHECK (total_amount >= 0.00)
) ENGINE=InnoDB;

-- -----------------------------------------------------------------------------
-- 6. TABLE: CANCELLATION (FR-07: Manage Cancellations & Refunds)
-- -----------------------------------------------------------------------------
CREATE TABLE cancellation (
    cancellation_id INT AUTO_INCREMENT PRIMARY KEY,
    booking_id INT NOT NULL UNIQUE,
    cancellation_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    refund_amount DECIMAL(10, 2) NOT NULL,
    cancellation_fee DECIMAL(10, 2) NOT NULL,
    CONSTRAINT fk_cancellation_booking FOREIGN KEY (booking_id) REFERENCES booking(booking_id) ON DELETE CASCADE,
    CONSTRAINT chk_refund_non_negative CHECK (refund_amount >= 0.00),
    CONSTRAINT chk_fee_non_negative CHECK (cancellation_fee >= 0.00)
) ENGINE=InnoDB;

-- -----------------------------------------------------------------------------
-- 7. TABLE: PAYMENT (Transaction settlement)
-- -----------------------------------------------------------------------------
CREATE TABLE payment (
    payment_id INT AUTO_INCREMENT PRIMARY KEY,
    booking_id INT NOT NULL,
    payment_mode ENUM('CREDIT_CARD', 'DEBIT_CARD', 'UPI', 'NET_BANKING', 'CASH') NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    payment_status ENUM('SUCCESS', 'REFUNDED') NOT NULL DEFAULT 'SUCCESS',
    transaction_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_payment_booking FOREIGN KEY (booking_id) REFERENCES booking(booking_id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- -----------------------------------------------------------------------------
-- INDEXES FOR QUERY OPTIMIZATION (Unit 3 Alignment)
-- -----------------------------------------------------------------------------
CREATE INDEX idx_show_movie ON show_schedule(movie_id);
CREATE INDEX idx_show_date ON show_schedule(show_date);
CREATE INDEX idx_booking_show ON booking(show_id);
CREATE INDEX idx_booking_status ON booking(booking_status);

-- -----------------------------------------------------------------------------
-- DATABASE TRIGGERS (Unit 2 Alignment)
-- -----------------------------------------------------------------------------
DELIMITER //

-- Trigger 1: Automatically decrement available seats upon confirmed booking
CREATE TRIGGER trg_after_booking_insert
AFTER INSERT ON booking
FOR EACH ROW
BEGIN
    IF NEW.booking_status = 'CONFIRMED' THEN
        UPDATE show_schedule
        SET available_seats = available_seats - NEW.number_of_tickets
        WHERE show_id = NEW.show_id;
    END IF;
END //

-- Trigger 2: Automatically restore available seats upon booking cancellation
CREATE TRIGGER trg_after_cancellation_insert
AFTER INSERT ON cancellation
FOR EACH ROW
BEGIN
    DECLARE v_show_id INT;
    DECLARE v_tickets INT;

    SELECT show_id, number_of_tickets INTO v_show_id, v_tickets
    FROM booking
    WHERE booking_id = NEW.booking_id;

    UPDATE show_schedule
    SET available_seats = available_seats + v_tickets
    WHERE show_id = v_show_id;

    UPDATE booking
    SET booking_status = 'CANCELLED'
    WHERE booking_id = NEW.booking_id;
END //

DELIMITER ;

-- -----------------------------------------------------------------------------
-- DATABASE VIEWS (FR-09 & FR-10: Reports and Data Science Export)
-- -----------------------------------------------------------------------------

-- View 1: Show-wise Bookings, Available Seats & Occupancy Rate
CREATE OR REPLACE VIEW vw_show_occupancy_report AS
SELECT 
    s.show_id,
    m.title AS movie_title,
    m.genre,
    m.language,
    sc.screen_name,
    sc.seating_capacity,
    s.show_date,
    s.show_time,
    s.ticket_price,
    s.available_seats,
    (sc.seating_capacity - s.available_seats) AS booked_seats,
    ROUND(((sc.seating_capacity - s.available_seats) / sc.seating_capacity) * 100, 2) AS occupancy_percentage
FROM show_schedule s
JOIN movie m ON s.movie_id = m.movie_id
JOIN screen sc ON s.screen_id = sc.screen_id;

-- View 2: Movie-wise Box Office Revenue Summary (Accounting for refunds)
CREATE OR REPLACE VIEW vw_movie_revenue_summary AS
SELECT 
    m.movie_id,
    m.title AS movie_title,
    m.genre,
    m.language,
    COUNT(DISTINCT s.show_id) AS total_shows_screened,
    COALESCE(SUM(CASE WHEN b.booking_status = 'CONFIRMED' THEN b.number_of_tickets ELSE 0 END), 0) AS total_tickets_sold,
    COALESCE(SUM(b.total_amount), 0) AS gross_revenue,
    COALESCE(SUM(c.refund_amount), 0) AS total_refunds,
    (COALESCE(SUM(b.total_amount), 0) - COALESCE(SUM(c.refund_amount), 0)) AS net_revenue
FROM movie m
LEFT JOIN show_schedule s ON m.movie_id = s.movie_id
LEFT JOIN booking b ON s.show_id = b.show_id
LEFT JOIN cancellation c ON b.booking_id = c.booking_id
GROUP BY m.movie_id, m.title, m.genre, m.language;

-- View 3: Complete Denormalized Flat View for Python Data Science Analysis
CREATE OR REPLACE VIEW vw_theatre_analytics_export AS
SELECT 
    b.booking_id,
    b.booking_date,
    c.customer_name,
    m.movie_id,
    m.title AS movie_title,
    m.genre,
    m.language,
    m.duration_minutes,
    sc.screen_id,
    sc.screen_name,
    sc.seating_capacity,
    s.show_id,
    s.show_date,
    s.show_time,
    s.ticket_price,
    b.number_of_tickets,
    b.total_amount AS gross_amount,
    b.booking_status,
    COALESCE(can.refund_amount, 0.00) AS refund_amount,
    (b.total_amount - COALESCE(can.refund_amount, 0.00)) AS net_amount,
    ROUND(((sc.seating_capacity - s.available_seats) / sc.seating_capacity) * 100, 2) AS show_occupancy_pct
FROM booking b
JOIN customer c ON b.customer_id = c.customer_id
JOIN show_schedule s ON b.show_id = s.show_id
JOIN movie m ON s.movie_id = m.movie_id
JOIN screen sc ON s.screen_id = sc.screen_id
LEFT JOIN cancellation can ON b.booking_id = can.booking_id;
