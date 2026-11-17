# CATEGORY 2: DATABASE MANAGEMENT SYSTEMS (Course Code: 25ITT31)
## Detailed Requirements Evaluation & Syllabus Alignment Document

### Project Title:
**Movie Theatre Ticket Booking and Box Office Analysis System**
*Assigned Register Numbers:* 30, 60, 90, 120  
**Institution:** Velalar College of Engineering and Technology, Erode - 12  
**Department:** Information Technology | **Class:** 25IT3A & 25IT3B

---

## 1. Executive Summary
In cinema ticket booking and box office operations, the database management system represents the backbone of truth. A robust relational model is imperative to eliminate data redundancy, enforce strict referential integrity, handle concurrent seat bookings safely through ACID-compliant transactions, and provide performant analytical views for management reporting.

---

## 2. Direct Syllabus Mapping (25ITT31: Database Management Systems)

| Syllabus Unit | Core Theoretical Concepts in Syllabus | Scenario 30 Implementation & Deliverable |
| :--- | :--- | :--- |
| **Unit 1: Database Systems and Design** | E-R Modeling, Entities, Attributes (Composite, Multivalued, Derived), Mapping Cardinalities, Relational Model, Keys (Candidate, Primary, Foreign), Relational Algebra. | **ER & Relational Model Document:** Complete ER Diagram with cardinalities, relational schema definition, primary/foreign key mappings, and relational algebra operations for queries. |
| **Unit 2: SQL** | DDL, DML, Data types, Aggregate functions, Nested subqueries, Joins (Inner, Outer), Views, Integrity Constraints, Indexes, Triggers, Sequences. | **SQL Scripts (`schema.sql`, `sample_data.sql`, `queries_and_reports.sql`):** Tables with constraints, triggers for seat updating on booking/cancellation, views for occupancy/revenue, complex joins, and subqueries. |
| **Unit 3: Normalization & Query Optimization** | Functional Dependencies, Armstrong's Axioms, Normal Forms (1NF, 2NF, 3NF, BCNF), Denormalization, Query Processing & Indexing. | **Normalization Document:** Step-by-step normalization proofs from UNF to 3NF/BCNF, dependency preservation, lossless join decomposition, B+ Tree indexing strategy. |
| **Unit 4: Transaction Management** | ACID properties, Transaction states, Concurrency Control, Serializability, Lock-based protocols, Deadlock handling. | **Transaction Specification:** ACID analysis for concurrent ticket reservations, strict Two-Phase Locking (2PL), row-level locks on shows/seats to prevent double-booking. |
| **Unit 5: NoSQL Databases** | BASE Properties, CAP Theorem, Document-based (MongoDB), Key-Value, Columnar, Graph stores. | **Architectural Comparison:** Comparative evaluation of RDBMS vs Document NoSQL (MongoDB) for storing dynamic ticket JSON payloads and audit logs. |

---

## 3. Evaluation of Scenario 30 Requirements under DBMS

### 3.1 Entity Identification & Mapping
- `MOVIE`: Stores master movie attributes (Movie ID, Title, Genre, Language, Duration, Release Date).
- `SCREEN`: Represents physical auditorium rooms (Screen ID, Screen Name, Seating Capacity).
- `SHOW_SCHEDULE`: Associates a Movie with a Screen, Show Date, Show Time, Base Ticket Price, and Current Available Seats.
- `CUSTOMER`: Captures customer identity (Customer ID, Name, Phone, Email).
- `BOOKING`: Records a ticket purchase transaction (Booking ID, Customer ID, Show ID, Number of Seats, Total Amount, Booking Date, Status).
- `CANCELLATION`: Tracks cancelled bookings (Cancellation ID, Booking ID, Cancellation Date, Refund Amount, Cancellation Fee).
- `PAYMENT`: Records financial settlement (Payment ID, Booking ID, Payment Mode, Amount, Status).

### 3.2 Integrity Constraints Enforced
1. **Entity Integrity:** All primary keys (`movie_id`, `screen_id`, `show_id`, `booking_id`, `cancellation_id`) are non-null and uniquely indexed.
2. **Referential Integrity:**
   - `show_schedule.movie_id` references `movie.movie_id` ON DELETE RESTRICT.
   - `show_schedule.screen_id` references `screen.screen_id` ON DELETE RESTRICT.
   - `booking.show_id` references `show_schedule.show_id` ON DELETE RESTRICT.
   - `booking.customer_id` references `customer.customer_id` ON DELETE CASCADE.
   - `cancellation.booking_id` references `booking.booking_id` ON DELETE CASCADE.
3. **Domain & Check Constraints:**
   - `seating_capacity > 0`
   - `ticket_price > 0.00`
   - `available_seats >= 0` (Prevents overbooking at the schema level)
   - `booking_status IN ('CONFIRMED', 'CANCELLED')`

### 3.3 Triggers and Automation
- **Trigger `trg_after_booking`:** When a row is inserted into `booking`, the trigger automatically decrements `available_seats` in `show_schedule`.
- **Trigger `trg_after_cancellation`:** When a booking is cancelled, the trigger automatically restores `available_seats` in `show_schedule` by adding the booked tickets back.

---

## 4. Course Outcomes (CO) Addressed
- **CO1:** Design a database schema using E-R model, Relational model, and apply relational algebra operations.
- **CO2:** Develop SQL queries using aggregate functions, nested subqueries, joins, and views.
- **CO3:** Apply suitable normalization and query optimization techniques to normalize relations and optimize query access.
- **CO4:** Discuss serialization, ACID properties, and concurrency control mechanisms to avoid race conditions.
- **CO5:** Describe and compare relational systems with NoSQL database paradigms.
