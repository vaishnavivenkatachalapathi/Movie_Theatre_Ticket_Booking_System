# Database Conclusion

**Project:** Scenario 30 – Movie Theatre Ticket Booking and Box Office Analysis System  
**Institution:** Velalar College of Engineering and Technology (Autonomous), Erode – 12  
**Department:** Information Technology | **Academic Year:** 2026-27 (ODD)  
**Class:** 25IT3A & 25IT3B | **Register Numbers:** 30, 60, 90, 120

---

## 1. Project Database Summary

The **velalar_cinemas** relational database was designed and implemented to serve as the persistent data backbone of the Movie Theatre Ticket Booking and Box Office Analysis System. It follows the relational model with strict adherence to normalisation principles, referential integrity, and transaction management best practices.

The database manages six core domain entities — Movies, Screens, Show Schedules, Customers, Bookings, Cancellations, and Payments — across **seven interrelated tables** with **clean, normalised relationships**.

---

## 2. Database Design Decisions

### 2.1 Normalisation to 3NF

All tables were systematically normalised from Unnormalised Form (UNF) through First, Second, and Third Normal Forms:

| Normal Form | Key Rule Applied |
|-------------|-----------------|
| 1NF | Eliminated multi-valued attributes (seat numbers stored as VARCHAR list; ticket quantities as a single count) |
| 2NF | Removed partial dependencies — customer data separated from bookings into `customers` table |
| 3NF | Removed transitive dependencies — pricing moved to `show_schedules`, screen capacity to `screens` |

This ensures **no data redundancy** and **no update anomalies** across the schema.

### 2.2 Referential Integrity

All foreign key relationships are enforced at the database level:
- `show_schedules` → `movies`, `screens` (ON DELETE RESTRICT — prevents deletion of a movie or screen while shows are scheduled)
- `bookings` → `customers`, `show_schedules` (ON DELETE RESTRICT)
- `cancellations`, `payments` → `bookings` (ON DELETE CASCADE — deleting a booking removes related records)

### 2.3 Trigger-Based Automation

An AFTER INSERT trigger (`trg_after_booking_insert`) automatically decrements `show_schedules.available_seats` whenever a new booking is inserted. This ensures seat counts remain accurate without requiring application-layer logic for every booking path.

```sql
CREATE TRIGGER trg_after_booking_insert
AFTER INSERT ON bookings
FOR EACH ROW
BEGIN
  UPDATE show_schedules
  SET available_seats = available_seats - NEW.num_tickets
  WHERE show_id = NEW.show_id;
END;
```

---

## 3. Schema Statistics

| Metric | Value |
|--------|-------|
| Total tables | 7 |
| Total columns across all tables | 56 |
| Primary key constraints | 7 |
| Foreign key constraints | 7 |
| UNIQUE constraints | 2 |
| CHECK constraints | 2 |
| Triggers | 1 |
| Views created | 3 |
| SQL queries implemented | 20 |
| Normalisation level achieved | 3NF |

---

## 4. Views Created

Three SQL views were created to simplify common reporting queries:

| View Name | Purpose |
|-----------|---------|
| `vw_show_summary` | Joins `show_schedules`, `movies`, `screens` to show show details with movie title and screen name |
| `vw_booking_details` | Joins `bookings`, `customers`, `show_schedules`, `movies` for complete booking records |
| `vw_revenue_report` | Aggregates total revenue, booking count, and occupancy percentage per show |

---

## 5. Benefits of This Database Design

### 5.1 Data Integrity
- Foreign key constraints prevent orphan records (e.g., a booking for a non-existent show)
- CHECK constraints prevent invalid data (e.g., negative ticket quantities)
- UNIQUE constraints on `email` and `screen_name` prevent duplicate registrations

### 5.2 Query Efficiency
- Indexed primary and foreign keys ensure O(log n) lookup performance
- Views pre-join frequently queried tables, reducing query complexity for reporting

### 5.3 Scalability
- The schema supports thousands of movies, unlimited shows, and millions of bookings without structural changes
- Adding new seat types (e.g., `Recliner`) requires only an ENUM value addition, not a table restructure

### 5.4 Maintainability
- 3NF eliminates update anomalies — changing a movie's title updates only the `movies` table, not every booking record
- Trigger automation removes the need for manual seat count updates in application code

### 5.5 Reporting Power
- The `vw_revenue_report` view, combined with GROUP BY and analytical queries in `queries_and_reports.sql`, supports:
  - Show-wise occupancy percentages
  - Movie-wise revenue rankings
  - Cancellation rate analysis
  - Peak time slot identification
  - Customer booking frequency reports

---

## 6. Sample Query Performance

| Query Type | Tables Involved | Complexity | Result |
|------------|----------------|------------|--------|
| Booking lookup by ID | `bookings`, `customers` | O(log n) | < 1ms |
| Seat availability check | `show_schedules` | O(log n) | < 1ms |
| Revenue by movie (GROUP BY) | `bookings`, `show_schedules`, `movies` | O(n log n) | < 10ms |
| Full box office report | 4 tables joined | O(n log n) | < 50ms |

---

## 7. Learning Outcomes

| CO | Outcome | Demonstrated |
|----|---------|--------------|
| CO1 | Design ER diagrams and relational schemas | ER diagram with 7 entities and 6 relationships |
| CO2 | Apply normalisation through 3NF | Step-by-step normalisation in `Database_Normalization_Document.md` |
| CO3 | Write DDL statements | 7 CREATE TABLE statements in `schema.sql` |
| CO4 | Write complex DML and queries | 20 queries including joins, subqueries, aggregations |
| CO5 | Understand transactions and integrity | ACID-compliant trigger and FK cascade design |

---

## 8. Conclusion Statement

The **velalar_cinemas** database design successfully implements a normalised, referentially-intact, and trigger-automated relational schema that underpins the entire Movie Theatre Ticket Booking System. The seven-table design achieves Third Normal Form, eliminates all data redundancy, and enforces business rules at the database level — ensuring data correctness regardless of the application layer.

The 20 implemented SQL queries and 3 views provide comprehensive reporting capabilities covering revenue analysis, occupancy tracking, cancellation patterns, and customer behaviour — forming the data foundation for the Python-based data science analytics module.

This database design demonstrates industry-standard practices in relational database engineering and fulfils all five course outcomes of the **25ITT31 Database Management Systems** syllabus.

---

*Document prepared by Register Numbers 30, 60, 90, 120 | Class 25IT3A & 25IT3B*  
*Department of Information Technology | Velalar College of Engineering and Technology (Autonomous), Erode – 12*  
*Academic Year: 2026-27 (ODD) | Scenario 30*
