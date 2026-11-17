# Movie Theatre Ticket Booking and Box Office Analysis System
### Scenario 30 | Academic Year: 2026 - 2027 (ODD Semester) | Class: 25IT3A & 25IT3B
**Institution:** VELALAR COLLEGE OF ENGINEERING AND TECHNOLOGY (AUTONOMOUS), ERODE - 12  
**Department:** Information Technology  
**Assigned Register Numbers:** 30, 60, 90, 120  

---

## 📽️ Project Overview
Cinema multiplexes manage complex operational workflows involving film distribution catalogs, multi-auditorium screen layouts, dynamic show scheduling, real-time seat inventories, customer ticket reservations, cancellations, and ticket revenues. Manual recordkeeping frequently results in double-booking errors, inability to monitor available seats in real-time, and a lack of data-driven insight into movie popularity, screen utilization, and box office earnings.

This project delivers an end-to-end engineering solution that integrates:
1. **Software Engineering (25ITT33):** Requirements elicitation, IEEE 830 SRS, UML architectural design (Use Case, Class, Sequence, State diagrams), and comprehensive verification & validation (Black-box & White-box test cases).
2. **Database Management Systems (25ITT31):** Conceptual E-R modeling, relational schemas with integrity constraints, normalization from UNF up to BCNF, transaction management/ACID properties, and full SQL implementations (DDL, DML, triggers, views, and complex queries).
3. **Java Programming (25ITT32):** Modular Object-Oriented console application (JDK 27) implementing inheritance, custom exception handling, thread-safe synchronized booking to prevent race conditions, and Java Stream API / Lambdas for operational reporting.
4. **Foundations of Data Science (25ITC31):** Data retrieval, cleaning, IQR outlier detection, feature engineering, descriptive statistical analysis (central tendency, variability, Z-scores), and publication-ready visualizations using NumPy, Pandas, Matplotlib, and Seaborn.

---

## 📁 Repository Directory Structure

```text
c:\Activity\
│
├── 1_Software_Engineering/
│   ├── Requirements_and_Syllabus_Mapping.md  # Detailed SE syllabus evaluation (Units 1-5, COs, FP & COCOMO)
│   ├── SRS_Document.md                       # IEEE Std 830-1998 compliant Software Requirements Specification
│   ├── System_Architecture_and_Design.md    # 7 Required Diagrams in recommended order & detailed specs
│   ├── Software_Testing_Document.md         # V&V Test Plan, Black-box (BVA/EP) & White-box test cases & metrics
│   ├── Use Case Description.md              # Actors catalog + step-by-step descriptions for UC-01 to UC-11
│   ├── Test Cases.xlsx                      # 16 manual test cases: ID, Category, Input, Expected, Actual, Status
│   ├── Conclusion.md                        # Project summary, results, learning outcomes, future enhancements
│   ├── Testing Screenshots/                 # 7 terminal SVG screenshots + README index
│   │   ├── screenshot_01_main_menu.svg
│   │   ├── screenshot_02_show_schedules.svg
│   │   ├── screenshot_03_booking_receipt.svg
│   │   ├── screenshot_04_overbooking_error.svg
│   │   ├── screenshot_05_refund_slip.svg
│   │   ├── screenshot_06_test_results.svg
│   │   ├── screenshot_07_occupancy_report.svg
│   │   └── README.md
│   └── diagrams/                            # High-resolution vector diagrams (SVG):
│       ├── 1_use_case_diagram.svg           # 1. Use Case Diagram
│       ├── 2_class_diagram.svg              # 2. Class Diagram
│       ├── 3_system_architecture_diagram.svg# 3. System Architecture Diagram
│       ├── 4_dfd_level_0.svg                # 4. Data Flow Diagram (DFD) - Level 0
│       ├── 5_dfd_level_1.svg                # 5. Data Flow Diagram (DFD) - Level 1
│       ├── 6_activity_diagram.svg           # 6. UML Activity Diagram (Booking & Cancellation)
│       └── 7_sequence_diagram.svg           # 7. UML Sequence Diagram (Lifelines & Concurrency)
│
├── 2_Database_Management_Systems/
│   ├── run_dbms.bat                         # Double-click runnable to launch Interactive SQL Query Simulator
│   ├── program_execution_output.txt         # Text execution output of SQL queries, triggers & transactions
│   ├── InteractiveDBSimulator.java          # Interactive SQL engine supporting manual inputs & transaction testing
│   ├── DBMS_Syllabus_Evaluation.md          # Detailed DBMS syllabus evaluation (Units 1-5, COs, Transactions)
│   ├── Data Dictionary.md                   # Full documentation of all 7 tables, columns, constraints & keys
│   ├── Database Conclusion.md               # Summary of 3NF design, trigger automation, and operational benefits
│   ├── ER_Diagram_and_Relational_Model.md   # Conceptual E-R diagram, cardinalities, relational algebra queries
│   ├── er_diagram.svg                       # High-resolution vector Entity-Relationship (E-R) Diagram
│   ├── Database_Normalization_Document.md   # Step-by-step normalization proofs (UNF -> 1NF -> 2NF -> 3NF -> BCNF)
│   ├── schema.sql                           # DDL script: Tables, PK/FK constraints, Checks, Indexes, Triggers, Views
│   ├── sample_data.sql                      # DML script: Populates movies, screens, shows, bookings, cancellations
│   ├── queries_and_reports.sql              # Advanced queries: Aggregates, Joins, Subqueries, Occupancy, Revenue
│   └── SQL Query Outputs/                   # Rendered terminal query outputs (SVG format)
│       ├── sql_screenshot_execution.svg
│       ├── sql_output_01_revenue_and_cancellation.svg
│       └── sql_output_02_occupancy_view.svg
│
├── 3_Java_Programming/
│   ├── run_java.bat                         # Double-click runnable to launch Interactive Java Booking System
│   ├── program_execution_output.txt         # Full text execution log of menus, booking, overbooking & test runs
│   ├── Java README.md                       # Comprehensive guide to compile, test, and run the Java app
│   ├── Java Program Explanation.md          # In-depth OOP analysis, class hierarchy, exceptions & streams
│   ├── Java Testing Results.md              # 7/7 test suite execution log, timings & assertion proof
│   ├── Java_Syllabus_Evaluation.md          # Detailed Java syllabus evaluation (Units 1-5, COs, OOP, Streams)
│   ├── JDBC_Driver_Setup.md                 # Complete JDBC driver setup, offline JAR config & connection guide
│   ├── Sample_Inputs_and_Outputs.md         # Complete console execution transcripts and verified test runs
│   ├── Java Screenshots/                    # Console snapshots of admin panel, movies, bookings & compilation
│   │   ├── java_screenshot_01_admin_panel.svg
│   │   ├── java_screenshot_02_add_movie.svg
│   │   ├── java_screenshot_03_booking_history.svg
│   │   ├── java_screenshot_04_compilation.svg
│   │   └── README.md
│   ├── bin/                                 # Compiled Java bytecode (.class files)
│   └── src/
│       └── com/theatre/
│           ├── model/                       # Domain models: Person, Customer, Movie, Screen, ShowSchedule, etc.
│           ├── exception/                   # User-defined exceptions: SeatUnavailableException, etc.
│           ├── service/                     # Service interfaces & implementations (BookingService, ReportService)
│           ├── concurrency/                 # Multithreaded concurrent booking simulator
│           ├── dao/                         # JDBC database persistence layer
│           ├── test/                        # Automated unit regression test suite
│           └── app/                         # Master entry point: MovieTheatreApp.java
│
├── 4_Foundation_of_Data_Science/
│   ├── run_datascience.bat                  # Double-click runnable to launch Interactive Data Analytics Engine
│   ├── program_execution_output.txt         # Complete text report of statistical metrics, popularity & trends
│   ├── InteractiveAnalytics.java            # Interactive analytics program supporting manual inputs & filtering
│   ├── Movie Popularity (Tickets Sold).svg  # Comparative volume chart of tickets sold per film
│   ├── Daily Booking Trends.svg             # Time-series trend of day-by-day bookings & ticket velocity
│   ├── FDS_Syllabus_Evaluation.md           # Detailed FDS syllabus evaluation (Units 1-5, COs, NumPy, Pandas)
│   ├── Data_Science_Analysis_Report.md      # Statistical report: Central tendency, Z-scores, Pivots, EDA findings
│   ├── theatre_bookings.csv                 # Ingested dataset: Show details, bookings, refunds, occupancy rates
│   ├── dataset_generator.py                 # Script for data export and database pipeline interfacing
│   ├── box_office_analysis.py               # Complete Data Science EDA, wrangling, and visualization script
│   ├── Screenshots/                         # Screenshots of interactive program execution
│   │   └── fds_screenshot_execution.svg
│   └── visualizations/                      # High-resolution generated charts
│       ├── 1_movie_revenue_comparison.svg
│       ├── 2_genre_revenue_distribution.svg
│       ├── 3_screen_occupancy_boxplot.svg
│       ├── 4_correlation_heatmap.svg
│       ├── 5_price_vs_occupancy_scatter.svg
│       ├── 6_timeslot_occupancy.svg
│       ├── interactive_dashboard.html
│       └── README.md
│
└── README.md                                # Master project documentation & execution guide
```

---

## 🛠️ Technology Stack

| Domain | Technology / Tool | Version | Purpose |
| :--- | :--- | :--- | :--- |
| **System Modeling** | IEEE 830, Mermaid UML, Markdown | Standard | Requirements, Architecture, Class, and Sequence Design |
| **Core Application** | Java SE (JDK) | JDK 27 | OOP Business Logic, Thread Synchronization, Stream API |
| **Database** | MySQL / Relational SQL | 8.0+ | Relational Persistence, Triggers, Views, Normalization |
| **Data Analysis** | Python (Pandas, NumPy) | 3.10+ | Data Wrangling, Cleaning, Outlier Treatment, Z-Scores |
| **Visualization** | Matplotlib, Seaborn | Latest | Box Plots, Correlation Heatmaps, Regplots, Bar/Pie Charts |

---

## 🚀 Execution & Setup Instructions (Double-Click Runnables)

Each programming domain now contains its own dedicated runnable `.bat` file inside its folder, allowing manual input testing without requiring internet access or extra tools:

### 1. Database Management Systems (DBMS)
Double-click `c:\Activity\2_Database_Management_Systems\run_dbms.bat`:
- Launches the interactive SQL query and ACID transaction engine.
- Supports manual inputs for booking transactions, seat decrement triggers, and movie catalog inserts.
- Saves execution output to `program_execution_output.txt`.

### 2. Java Programming
Double-click `c:\Activity\3_Java_Programming\run_java.bat`:
- Launches the master `MovieTheatreApp` console system with 10 interactive menus.
- Supports manual input for browsing shows, booking seats, testing overbooking exceptions, and tiered refunds.
- Saves execution output to `program_execution_output.txt`.

### 3. Foundations of Data Science (FDS)
Double-click `c:\Activity\4_Foundation_of_Data_Science\run_datascience.bat`:
- Ingests `theatre_bookings.csv` and presents an interactive menu.
- Supports manual queries for movie ticket rankings, daily trends, genre filtering, and Z-score calculations.
- Saves execution output to `program_execution_output.txt`.

---

## 📊 Summary of Syllabus Alignment Across Courses

| Course Code & Name | Key Deliverables & Syllabus Integration |
| :--- | :--- |
| **25ITT33: Software Engineering** | - IEEE 830 SRS with 10 Functional Requirements & Non-Functional Requirements<br>- 3-Tier Layered Architecture & Use Case Specifications<br>- Class, Sequence, and State Transition Diagrams<br>- Test Strategy, BVA/EP Test Cases, Defect Density Metrics, FP & COCOMO estimations |
| **25ITT31: Database Management Systems** | - Conceptual E-R Model with mapping cardinalities & relational algebra queries<br>- Step-by-step Normalization from UNF up to 3NF/BCNF (Lossless Join & Dependency Preservation)<br>- ACID Transaction & Lock-based Concurrency Control specifications<br>- SQL DDL, DML, Triggers (`trg_after_booking_insert`), Views, and Analytical Queries |
| **25ITT32: Java Programming** | - OOP Class Design, Inheritance (`Person` $\to$ `Customer`), Dynamic Method Dispatch<br>- Custom User Exceptions (`SeatUnavailableException`, `InvalidBookingException`)<br>- Thread Synchronization (`synchronized reserveSeats()`) preventing double booking<br>- Java Stream API & Lambdas for show occupancy and movie revenue reporting<br>- `StringBuilder` formatting for ticket receipts and cancellation slips |
| **25ITC31: Foundations of Data Science** | - Data ingestion, cleaning, duplicate removal, and missing value treatment<br>- IQR-based statistical outlier identification and standardized $Z$-scores<br>- NumPy array operations & Pandas DataFrames with multi-level GroupBy & Pivot Tables<br>- Six publication-quality visualizations using Matplotlib & Seaborn |
