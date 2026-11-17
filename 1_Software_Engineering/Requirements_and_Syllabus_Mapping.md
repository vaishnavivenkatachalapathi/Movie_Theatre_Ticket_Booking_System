# VELALAR COLLEGE OF ENGINEERING AND TECHNOLOGY (AUTONOMOUS), ERODE - 12
### DEPARTMENT OF INFORMATION TECHNOLOGY
### ACADEMIC YEAR: 2026 - 2027 (ODD SEMESTER) | CLASS: 25IT3A & 25IT3B
### PROJECT-BASED ASSESSMENT (SCENARIO 30)

---

# CATEGORY 1: SOFTWARE ENGINEERING (Course Code: 25ITT33)
## Detailed Requirements Evaluation & Syllabus Alignment Document

### Project Title:
**Movie Theatre Ticket Booking and Box Office Analysis System**
*Assigned Register Numbers:* 30, 60, 90, 120

---

## 1. Executive Summary & Problem Context
Manual recordkeeping in cinema multiplexes leads to frequent booking anomalies, overbooking, lack of real-time seat availability tracking, inefficient cancellation handling, and zero visibility into occupancy rates and revenue performance. 
From a Software Engineering perspective, this project demands a systematic, engineering-disciplined approach across the entire software development lifecycle (SDLC), ensuring rigorous requirement elicitation, structured architectural design, verification & validation, and quality metrics tracking.

---

## 2. Direct Syllabus Mapping (25ITT33: Software Engineering)

| Syllabus Unit | Core Theoretical Concepts in Syllabus | Project Implementation / Artifact |
| :--- | :--- | :--- |
| **Unit 1: Software Process** | Nature of Software, Software Process Models, Prescriptive & Agile Models (Waterfall, Iterative, Agile Scrum, Unified Process). | **Process Model Selection:** Iterative / Agile Scrum Model with 2-week sprints delivering functional increments (Movie Catalog -> Scheduling -> Booking Engine -> Reporting & Analytics). |
| **Unit 2: Requirements** | Requirement Engineering (Elicitation, Specification, Validation), Class-Based Modeling, Data Modeling, IEEE SRS. | **SRS Document (IEEE 830 Standard):** Functional Requirements (FR1–FR10), Non-Functional Requirements (Performance, Reliability, Security, Scalability), Domain Modeling. |
| **Unit 3: Software Design** | Design Concepts, Architectural Design (Layered / MVC), Interface Analysis & Design, Component-Level Design. | **System Architecture & UML Design:** 3-Tier Layered Architecture (Presentation, Business Logic, Persistence), Use Case Diagrams with specifications, Detailed Class Diagram, Sequence Diagrams for Booking and Cancellation. |
| **Unit 4: Software Testing** | Verification & Validation, Types of Testing (Unit, Integration, System, Acceptance), Black-Box (BVA, Equivalence Partitioning), White-Box. | **Software Testing Document:** Comprehensive Test Plan, Test Case Suite covering positive, boundary, and negative scenarios (overbooking, refund rules), Defect Tracking, and Quality Metrics. |
| **Unit 5: Project Management** | 4P's (People, Product, Process, Project), LOC & Function Point (FP) Estimation, COCOMO Cost Modeling, Risk Management, CMMI. | **Project Estimation & Risk Assessment:** FP and COCOMO sizing, Work Breakdown Structure (WBS), Gantt Schedule, and Risk Identification & Mitigation Matrix. |

---

## 3. Evaluation of Scenario 30 Requirements under Software Engineering

### 3.1 Requirements Classification
The ten functional requirements specified in Scenario 30 are categorized into engineering modules:

1. **Movie & Catalog Management Module (FR1):**
   - Elicitation of attributes: Movie ID, title, genre, language, duration, release date.
   - Design of CRUD interfaces and validation of release status.
2. **Screen & Infrastructure Module (FR2):**
   - Maintenance of Screen ID, screen type (IMAX, 3D, Dolby Atmos, Standard), seating capacity.
   - Integrity constraint: Capacity must be strictly positive and bounded by theatre physical constraints.
3. **Show Scheduling Module (FR3):**
   - Matrix association: Movie + Screen + Date + Time Slot + Base Ticket Price.
   - Validation against overlapping show timings on the same screen.
4. **Seat Inventory & Real-Time Availability Module (FR4, FR6):**
   - Real-time seat tracking: Booked seats count vs Available seats count ($Available = Capacity - Booked$).
   - Strict validation preventing overbooking ($Requested \le Available$).
5. **Booking & Transaction Management Module (FR5, FR8):**
   - Booking generation: Unique Booking ID, Customer details, timestamp, ticket count, gross revenue computation.
6. **Cancellation & Refund Rule Engine (FR7):**
   - Business rule engine: Tiered refund calculations based on cancellation lead time relative to show time.
   - Automated seat inventory restoration.
7. **Reporting & Business Intelligence Module (FR9, FR10):**
   - Aggregated metrics: Show-wise occupancy percentage, movie-wise gross/net revenue, screen utilization ratios.

---

## 4. Software Project Estimation & Quality Planning (Unit 5 Alignment)

### 4.1 Function Point (FP) Estimation
- **External Inputs (EI):** Add Movie, Add Screen, Schedule Show, Book Tickets, Cancel Booking (5 inputs $\times$ Average Weight 4 = 20 FP)
- **External Outputs (EO):** Booking Receipt, Cancellation Refund Slip, Show Occupancy Report, Movie Revenue Summary (4 outputs $\times$ Average Weight 5 = 20 FP)
- **External Inquiries (EQ):** Query Available Seats, Search Movies by Genre/Date, Check Booking Status (3 inquiries $\times$ Average Weight 4 = 12 FP)
- **Internal Logical Files (ILF):** Movie Table, Screen Table, Show Schedule Table, Booking Table, Cancellation Table (5 ILFs $\times$ Average Weight 10 = 50 FP)
- **External Interface Files (EIF):** Payment Gateway Stub, Analytics Data Feed (2 EIFs $\times$ Average Weight 7 = 14 FP)
- **Unadjusted Function Points (UFP):** $20 + 20 + 12 + 50 + 14 = 116\text{ UFP}$
- With Value Adjustment Factor ($VAF \approx 1.05$), Adjusted FP = $116 \times 1.05 \approx 121.8\text{ FP}$.

### 4.2 Basic COCOMO Cost & Effort Modeling
Using the Organic Mode ($a=2.4, b=1.05$):
- Estimated Lines of Code (Java + SQL + Python): $\approx 2.5\text{ KLOC}$
- $\text{Effort} = 2.4 \times (2.5)^{1.05} \approx 6.27\text{ Person-Months}$
- $\text{Development Time} = 2.5 \times (6.27)^{0.38} \approx 5.02\text{ Months}$
- Recommended Team Size: 2 to 4 Student Engineers.

### 4.3 Risk Identification & Mitigation Matrix
| Risk ID | Risk Description | Severity | Likelihood | Mitigation Strategy |
| :--- | :--- | :--- | :--- | :--- |
| **R01** | Concurrency race condition during simultaneous seat selection | High | High | Enforce database-level row locking and thread synchronization in Java booking service. |
| **R02** | Inconsistent cancellation refund calculation | Medium | Medium | Centralize refund rules in a single deterministic business rule engine with unit test coverage. |
| **R03** | Schedule collision (two movies scheduled on same screen at same time) | High | Low | Implement validation logic checking screen start time and end time buffer (+30 mins cleaning). |
| **R04** | Data divergence between SQL database and Python analytics feed | Medium | Low | Use direct automated SQL extraction queries into Pandas DataFrames. |

---

## 5. Course Outcomes (CO) Addressed
- **CO1:** Understand the software engineering lifecycle and prescriptive/agile models for project delivery.
- **CO2:** Perform requirement engineering and draft professional IEEE SRS documents for real-world scenarios.
- **CO3:** Translate functional requirements into architectural and detailed object-oriented design models (UML).
- **CO4:** Design and execute verification and validation plans using industry-standard testing techniques.
- **CO5:** Apply project management estimation, scheduling, and risk mitigation methodologies.
