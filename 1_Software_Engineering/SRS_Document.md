# SOFTWARE REQUIREMENTS SPECIFICATION (SRS)
## For Movie Theatre Ticket Booking and Box Office Analysis System
### Prepared in accordance with IEEE Std 830-1998 Format
**Academic Year:** 2026 - 2027 (ODD Semester)  
**Institution:** Velalar College of Engineering and Technology, Erode - 12  
**Department:** Information Technology | **Class:** 25IT3A & 25IT3B  
**Scenario:** 30 (Register Nos: 30, 60, 90, 120)

---

## 1. INTRODUCTION

### 1.1 Purpose
This Software Requirements Specification (SRS) establishes the complete functional and non-functional requirements for the **Movie Theatre Ticket Booking and Box Office Analysis System**. This document serves as the formal baseline agreement between stakeholders, developers, database administrators, and QA testers for design, implementation, and verification.

### 1.2 Scope
The software system is an integrated cinema management platform engineered to:
- Maintain detailed catalogs of movies and multiplex physical screens.
- Manage show scheduling, time slot allocations, and dynamic seat availability.
- Automate ticket reservations, real-time capacity validations, and transaction processing.
- Manage booking cancellations and implement policy-driven refund calculations.
- Compute gross and net ticket revenues.
- Feed transaction records into a Python-powered Data Science pipeline to perform Exploratory Data Analysis (EDA) and visualize box office trends, occupancy metrics, and movie popularity.

### 1.3 Definitions, Acronyms, and Abbreviations
- **SRS:** Software Requirements Specification
- **FR:** Functional Requirement
- **NFR:** Non-Functional Requirement
- **DBMS:** Database Management System
- **ACID:** Atomicity, Consistency, Isolation, Durability
- **EDA:** Exploratory Data Analysis
- **COCOMO:** Constructive Cost Model
- **UML:** Unified Modeling Language
- **Lead Time:** The duration remaining between the cancellation request time and scheduled show start time.

### 1.4 References
- IEEE Recommended Practice for Software Requirements Specifications (IEEE Std 830-1998).
- Pressman, R. S., & Maxim, B. R., *Software Engineering: A Practitioner's Approach*, 9th Edition.
- VCET B.Tech IT Regulation 2025 Curriculum and Syllabus (25ITT33, 25ITT31, 25ITT32, 25ITC31).

### 1.5 Overview of the Document
Section 2 provides the overall description of the system, including user characteristics and operating constraints. Section 3 outlines the detailed functional requirements (FR1 through FR10) and non-functional requirements.

---

## 2. OVERALL DESCRIPTION

### 2.1 Product Perspective
The system operates as a 3-tier architectural solution:
- **Presentation Layer:** Console and CLI interfaces for customer booking and administrative operations.
- **Application Logic Layer (Java):** Enforces business rules, seat locking, transaction boundaries, and thread-safe booking processing.
- **Data Persistence Layer (SQL DBMS):** Relational schema guaranteeing ACID compliance, relational integrity, triggers, and views.
- **Analytics Subsystem (Python):** Ingests persisted datasets to execute statistical transformations and graphical visualizations.

```
+-------------------------------------------------------------+
|                     PRESENTATION TIER                       |
|   Admin CLI / Customer Booking Menu / Reports View          |
+------------------------------+------------------------------+
                               |
                               v
+-------------------------------------------------------------+
|                    APPLICATION LOGIC TIER                   |
|  Java Services (Booking, Cancellation, Schedule, Security)  |
|          Multi-threaded Concurrency & Exception Control     |
+------------------------------+------------------------------+
                               |
                               v
+-------------------------------------------------------------+
|                      DATA PERSISTENCE                       |
|      Relational Database (Tables, Views, Triggers, Checks)  |
+------------------------------+------------------------------+
                               |
                               v
+-------------------------------------------------------------+
|                 DATA SCIENCE & ANALYTICS                    |
|    Python (Pandas, NumPy, Matplotlib, Seaborn) Analytics    |
+-------------------------------------------------------------+
```

### 2.2 Product Functions
The primary functions supported by the system are:
1. Movie Information Management (CRUD operations on titles, genres, ratings, durations).
2. Screen & Seating Layout Configuration.
3. Show Scheduling with conflict avoidance.
4. Real-time dynamic seat inventory management.
5. Customer ticket reservation with payment calculation.
6. Overbooking prevention and capacity validation.
7. Booking cancellation with lead-time tiered refund policies.
8. Real-time net revenue computation.
9. Tabular report generation for management.
10. Python-based box office analytics and visualization.

### 2.3 User Classes and Characteristics
- **Customer / Ticket Buyer:** Views movies, schedules, available seats; books tickets; cancels tickets.
- **Theatre Administrator:** Manages movie catalog, configures screens, schedules show timings, sets ticket pricing, views operational reports.
- **Box Office Analyst:** Executes Python pipelines to extract datasets, conducts occupancy and revenue EDA, and generates strategic visualization charts.

### 2.4 Operating Environment
- Operating System: Cross-platform (Windows 10/11, Linux, macOS).
- Java Environment: JDK 17+ / JDK 27.
- Database: MySQL 8.0+ / Relational SQL Engine.
- Python Environment: Python 3.10+ with `pandas`, `numpy`, `matplotlib`, and `seaborn`.

### 2.5 Design and Implementation Constraints
- The system must prevent race conditions and double-booking when multiple users attempt to reserve the same seat simultaneously.
- Seating capacity is fixed per physical screen and cannot be exceeded under any circumstances.
- Refunds cannot exceed 100% of the booking cost and are strictly non-refundable once the show starts.

---

## 3. SPECIFIC REQUIREMENTS

### 3.1 External Interface Requirements
- **User Interfaces:** Clean, menu-driven CLI offering intuitive numerical option selection, input validation, and formatted tabular outputs.
- **Software Interfaces:** JDBC / JPA connectivity between Java core and SQL Database; CSV data export interface interfacing SQL and Python.

---

### 3.2 Functional Requirements (Scenario 30 Specifications)

#### FR-01: Manage Movie Details
- **Description:** System must maintain comprehensive attributes for each movie.
- **Inputs:** Movie Title, Genre (e.g., Action, Sci-Fi, Drama), Language (e.g., English, Tamil), Duration in minutes, Release Date.
- **Processing:** Assign unique sequential `movie_id`, validate duration $> 0$.
- **Outputs:** Confirmation of movie addition, formatted movie catalog list.

#### FR-02: Manage Theatre Screens
- **Description:** Maintain physical multiplex screens and their seating capacities.
- **Inputs:** Screen Name (e.g., Screen 1, IMAX Laser), Seating Capacity (e.g., 150).
- **Processing:** Verify capacity $> 0$; assign unique `screen_id`.
- **Outputs:** Registered screen details.

#### FR-03: Schedule Movie Shows
- **Description:** Bind a movie to a screen at a specific date and time with ticket pricing.
- **Inputs:** `movie_id`, `screen_id`, Show Date (YYYY-MM-DD), Show Time (HH:MM), Ticket Price.
- **Processing:** Validate that no other show is scheduled on the same screen with overlapping time windows; initialize available seats to screen capacity.
- **Outputs:** Unique `show_id` and active schedule record.

#### FR-04: Manage Seat Availability
- **Description:** Track booked seats and available seats in real-time.
- **Processing:** Dynamic computation: $AvailableSeats = ScreenCapacity - BookedSeats$. Update upon each booking and cancellation.
- **Outputs:** Current seat availability count and seat matrix status.

#### FR-05: Book Tickets
- **Description:** Allow customer to reserve seats for an active show.
- **Inputs:** `show_id`, Customer Name, Phone Number, Number of Tickets requested.
- **Processing:** Validate seat availability; generate `booking_id`; calculate total amount ($Tickets \times Price$); decrement available seats; record timestamp.
- **Outputs:** Booking confirmation receipt detailing Booking ID, Movie, Screen, Show Time, Seats, Total Price.

#### FR-06: Validate Bookings (Overbooking Prevention)
- **Description:** Prevent booking requests exceeding available seats.
- **Processing:** If $RequestedTickets > AvailableSeats$, raise `SeatUnavailableException` and abort transaction without state alteration.
- **Outputs:** Explicit error notification indicating remaining seats.

#### FR-07: Manage Cancellations & Refunds
- **Description:** Record cancelled bookings and apply defined refund rules.
- **Inputs:** `booking_id`.
- **Business Rules for Refunds:**
  - Cancellation $> 24\text{ hours}$ before show: $90\%$ refund ($10\%$ cancellation fee).
  - Cancellation between $2\text{ and }24\text{ hours}$ before show: $70\%$ refund ($30\%$ fee).
  - Cancellation $< 2\text{ hours}$ before show or post showtime: $0\%$ refund.
- **Processing:** Restore booked seats to available pool; calculate refund; update booking status to `CANCELLED`.
- **Outputs:** Cancellation receipt with refund amount.

#### FR-08: Calculate Revenue
- **Description:** Accurately calculate gross and net ticket revenue.
- **Processing:**
  - $Gross Revenue = \sum (\text{Confirmed Bookings} \times \text{Price})$
  - $Total Refunds = \sum (\text{Refund Amounts})$
  - $Net Revenue = Gross Revenue - Total Refunds$
- **Outputs:** Financial summary report per show, per movie, and overall theatre.

#### FR-09: Generate Reports
- **Description:** Produce operational management reports.
- **Outputs:**
  - Show-wise bookings and occupancy rate: $Occupancy\% = (Booked / Capacity) \times 100$.
  - Available seats per screen.
  - Movie-wise revenue rankings.

#### FR-10: Analyze Box Office Performance (Python EDA)
- **Description:** Ingest database records into Python to conduct data wrangling, descriptive statistics, and graphical analysis.
- **Outputs:** Visual charts for Movie Popularity, Occupancy trends, Revenue distributions, and Correlation Heatmaps.

---

## 4. NON-FUNCTIONAL REQUIREMENTS (NFR)

### 4.1 Performance Requirements
- **Response Time:** Console operations and database updates must execute within $< 200\text{ ms}$.
- **Throughput:** Capable of handling up to 100 concurrent booking requests per second without data inconsistency.

### 4.2 Reliability and Availability
- The system must ensure 99.9% uptime during operational hours.
- In the event of an unexpected runtime failure during checkout, the transaction must roll back cleanly (Atomicity).

### 4.3 Data Integrity & Security
- Relational foreign key constraints must be strictly enforced.
- Input sanitation must prevent SQL injection and invalid data format crashes.

### 4.4 Maintainability & Modularity
- Follow SOLID design principles, clean MVC architecture, and clear separation of concerns across packages.
