# Data Dictionary

**Project:** Scenario 30 – Movie Theatre Ticket Booking and Box Office Analysis System  
**Institution:** Velalar College of Engineering and Technology (Autonomous), Erode – 12  
**Department:** Information Technology | **Academic Year:** 2026-27 (ODD)  
**Class:** 25IT3A & 25IT3B | **Register Numbers:** 30, 60, 90, 120

---

## Overview

This data dictionary describes every table in the **velalar_cinemas** database schema. For each table it documents the purpose, all column names, data types, constraints, and the relationships to other tables.

**Database Name:** `velalar_cinemas`  
**RDBMS:** MySQL 8.0+  
**Character Set:** `utf8mb4`  
**Collation:** `utf8mb4_unicode_ci`  
**Total Tables:** 7

---

## Table 1: `movies`

**Purpose:** Stores the master catalogue of all movies screened at the cinema.

| Column Name | Data Type | Size | Constraints | Description |
|-------------|-----------|------|-------------|-------------|
| `movie_id` | VARCHAR | 10 | PRIMARY KEY | Unique identifier for each movie (e.g., `M001`) |
| `title` | VARCHAR | 150 | NOT NULL | Full title of the movie |
| `genre` | VARCHAR | 50 | NOT NULL | Genre category (Action, Drama, Comedy, etc.) |
| `language` | VARCHAR | 30 | NOT NULL | Language of the movie (Tamil, Hindi, English, etc.) |
| `duration_minutes` | INT | — | NOT NULL, CHECK > 0 | Runtime of the movie in minutes |
| `rating` | VARCHAR | 10 | DEFAULT 'U/A' | Censor board rating (U, U/A, A) |
| `release_date` | DATE | — | NOT NULL | Theatrical release date |
| `director` | VARCHAR | 100 | NOT NULL | Name of the director |
| `created_at` | TIMESTAMP | — | DEFAULT CURRENT_TIMESTAMP | Record creation timestamp |

**Primary Key:** `movie_id`  
**Foreign Keys:** None  
**Referenced By:** `show_schedules.movie_id`

---

## Table 2: `screens`

**Purpose:** Stores information about each physical cinema auditorium / screen hall.

| Column Name | Data Type | Size | Constraints | Description |
|-------------|-----------|------|-------------|-------------|
| `screen_id` | VARCHAR | 10 | PRIMARY KEY | Unique identifier (e.g., `SC01`) |
| `screen_name` | VARCHAR | 50 | NOT NULL, UNIQUE | Display name (e.g., `Screen-1`) |
| `total_seats` | INT | — | NOT NULL, CHECK > 0 | Physical seating capacity |
| `regular_seats` | INT | — | NOT NULL | Number of Regular-tier seats |
| `premium_seats` | INT | — | NOT NULL | Number of Premium-tier seats |
| `vip_seats` | INT | — | NOT NULL | Number of VIP-tier seats |
| `screen_type` | VARCHAR | 20 | DEFAULT 'Standard' | Projection type (Standard, Dolby, IMAX) |
| `is_active` | TINYINT | 1 | DEFAULT 1 | 1 = operational, 0 = under maintenance |

**Primary Key:** `screen_id`  
**Foreign Keys:** None  
**Referenced By:** `show_schedules.screen_id`

> [!NOTE] The constraint `regular_seats + premium_seats + vip_seats = total_seats` is enforced by a CHECK constraint in MySQL 8.0.

---

## Table 3: `show_schedules`

**Purpose:** Represents each individual show (a movie playing on a screen at a specific time). This is the central junction entity linking movies, screens, and bookings.

| Column Name | Data Type | Size | Constraints | Description |
|-------------|-----------|------|-------------|-------------|
| `show_id` | VARCHAR | 10 | PRIMARY KEY | Unique show identifier (e.g., `SH-01`) |
| `movie_id` | VARCHAR | 10 | NOT NULL, FK → movies | The movie being screened |
| `screen_id` | VARCHAR | 10 | NOT NULL, FK → screens | The screen/hall where it plays |
| `show_date` | DATE | — | NOT NULL | Calendar date of the show |
| `show_time` | TIME | — | NOT NULL | Scheduled start time |
| `available_seats` | INT | — | NOT NULL | Current available seats (decremented on booking) |
| `price_regular` | DECIMAL | (8,2) | NOT NULL | Ticket price for Regular seats (₹) |
| `price_premium` | DECIMAL | (8,2) | NOT NULL | Ticket price for Premium seats (₹) |
| `price_vip` | DECIMAL | (8,2) | NOT NULL | Ticket price for VIP seats (₹) |
| `status` | ENUM | — | DEFAULT 'SCHEDULED' | SCHEDULED / RUNNING / COMPLETED / CANCELLED |
| `created_at` | TIMESTAMP | — | DEFAULT CURRENT_TIMESTAMP | Record creation timestamp |

**Primary Key:** `show_id`  
**Foreign Keys:**
- `movie_id` → `movies(movie_id)` ON DELETE RESTRICT  
- `screen_id` → `screens(screen_id)` ON DELETE RESTRICT  

**Referenced By:** `bookings.show_id`

---

## Table 4: `customers`

**Purpose:** Stores customer personal information used for booking and communication.

| Column Name | Data Type | Size | Constraints | Description |
|-------------|-----------|------|-------------|-------------|
| `customer_id` | VARCHAR | 10 | PRIMARY KEY | Unique customer identifier (e.g., `CU-001`) |
| `full_name` | VARCHAR | 100 | NOT NULL | Customer's full name |
| `email` | VARCHAR | 150 | NOT NULL, UNIQUE | Email address (used for e-ticket delivery) |
| `phone` | VARCHAR | 15 | NOT NULL | Mobile phone number |
| `date_of_birth` | DATE | — | NULL | Optional date of birth |
| `loyalty_points` | INT | — | DEFAULT 0 | Accumulated loyalty reward points |
| `registered_at` | TIMESTAMP | — | DEFAULT CURRENT_TIMESTAMP | Account creation timestamp |

**Primary Key:** `customer_id`  
**Foreign Keys:** None  
**Referenced By:** `bookings.customer_id`

---

## Table 5: `bookings`

**Purpose:** Records every confirmed ticket booking transaction. This is the primary transactional table of the system.

| Column Name | Data Type | Size | Constraints | Description |
|-------------|-----------|------|-------------|-------------|
| `booking_id` | VARCHAR | 15 | PRIMARY KEY | Unique booking reference (e.g., `BK-2026-0001`) |
| `customer_id` | VARCHAR | 10 | NOT NULL, FK → customers | The customer who made the booking |
| `show_id` | VARCHAR | 10 | NOT NULL, FK → show_schedules | The show that was booked |
| `seat_type` | ENUM | — | NOT NULL | REGULAR / PREMIUM / VIP |
| `num_tickets` | INT | — | NOT NULL, CHECK > 0 | Number of tickets in this booking |
| `seat_numbers` | VARCHAR | 200 | NULL | Assigned seat labels (e.g., `P-12, P-13`) |
| `total_amount` | DECIMAL | (10,2) | NOT NULL | Total amount paid (₹) |
| `payment_method` | VARCHAR | 30 | DEFAULT 'CASH' | CASH / UPI / CARD / NETBANKING |
| `booking_status` | ENUM | — | DEFAULT 'CONFIRMED' | CONFIRMED / CANCELLED / NO_SHOW |
| `booked_at` | TIMESTAMP | — | DEFAULT CURRENT_TIMESTAMP | Booking transaction timestamp |

**Primary Key:** `booking_id`  
**Foreign Keys:**
- `customer_id` → `customers(customer_id)` ON DELETE RESTRICT  
- `show_id` → `show_schedules(show_id)` ON DELETE RESTRICT  

**Referenced By:** `cancellations.booking_id`, `payments.booking_id`

> [!IMPORTANT] An AFTER INSERT trigger (`trg_after_booking_insert`) automatically decrements `show_schedules.available_seats` when a new booking row is inserted.

---

## Table 6: `cancellations`

**Purpose:** Records cancellation events and the computed refund for each cancelled booking.

| Column Name | Data Type | Size | Constraints | Description |
|-------------|-----------|------|-------------|-------------|
| `cancellation_id` | VARCHAR | 15 | PRIMARY KEY | Unique cancellation reference (e.g., `CN-2026-0001`) |
| `booking_id` | VARCHAR | 15 | NOT NULL, FK → bookings | The original booking being cancelled |
| `cancellation_reason` | VARCHAR | 255 | NULL | Customer-provided reason |
| `cancellation_time` | TIMESTAMP | — | DEFAULT CURRENT_TIMESTAMP | Exact timestamp of cancellation |
| `hours_before_show` | DECIMAL | (5,2) | NOT NULL | Hours remaining until show at time of cancellation |
| `refund_tier` | TINYINT | — | NOT NULL | 1 (>24h, 90%), 2 (12–24h, 50%), 3 (<2h, 0%) |
| `refund_percentage` | DECIMAL | (5,2) | NOT NULL | Actual refund percentage applied |
| `refund_amount` | DECIMAL | (10,2) | NOT NULL | Computed refund amount (₹) |
| `refund_status` | ENUM | — | DEFAULT 'PENDING' | PENDING / PROCESSED / FAILED |

**Primary Key:** `cancellation_id`  
**Foreign Keys:**
- `booking_id` → `bookings(booking_id)` ON DELETE CASCADE  

---

## Table 7: `payments`

**Purpose:** Tracks the payment transaction record for each booking (and refund for cancellations).

| Column Name | Data Type | Size | Constraints | Description |
|-------------|-----------|------|-------------|-------------|
| `payment_id` | VARCHAR | 15 | PRIMARY KEY | Unique payment transaction ID |
| `booking_id` | VARCHAR | 15 | NOT NULL, FK → bookings | The booking this payment is for |
| `transaction_type` | ENUM | — | NOT NULL | PAYMENT / REFUND |
| `amount` | DECIMAL | (10,2) | NOT NULL | Transaction amount (₹) |
| `payment_method` | VARCHAR | 30 | NOT NULL | CASH / UPI / CARD / NETBANKING |
| `transaction_ref` | VARCHAR | 50 | NULL | External gateway reference number |
| `transaction_time` | TIMESTAMP | — | DEFAULT CURRENT_TIMESTAMP | Timestamp of the transaction |
| `status` | ENUM | — | DEFAULT 'SUCCESS' | SUCCESS / FAILED / PENDING |

**Primary Key:** `payment_id`  
**Foreign Keys:**
- `booking_id` → `bookings(booking_id)` ON DELETE CASCADE  

---

## Entity Relationship Summary

```
movies ──< show_schedules >── screens
               │
               │ (1 show → many bookings)
               ▼
customers ──< bookings >── payments
               │
               │ (1 booking → 0 or 1 cancellation)
               ▼
          cancellations
```

---

## Constraints Summary

| Constraint Type | Table | Column | Rule |
|-----------------|-------|--------|------|
| PRIMARY KEY | All 7 tables | `*_id` column | Uniquely identifies each row |
| FOREIGN KEY | `show_schedules` | `movie_id`, `screen_id` | Referential integrity to parent tables |
| FOREIGN KEY | `bookings` | `customer_id`, `show_id` | Referential integrity |
| FOREIGN KEY | `cancellations`, `payments` | `booking_id` | CASCADE delete |
| UNIQUE | `screens` | `screen_name` | No duplicate screen names |
| UNIQUE | `customers` | `email` | One account per email |
| CHECK | `movies` | `duration_minutes` | Must be > 0 |
| CHECK | `bookings` | `num_tickets` | Must be > 0 |
| NOT NULL | All tables | Key fields | Mandatory data |
| DEFAULT | Multiple | `status`, `created_at` | Automatic defaults |
| TRIGGER | `bookings` | `trg_after_booking_insert` | Auto-decrement available seats |

---

*Document prepared by Register Numbers 30, 60, 90, 120 | Class 25IT3A & 25IT3B*  
*Department of Information Technology | Velalar College of Engineering and Technology (Autonomous), Erode – 12*
