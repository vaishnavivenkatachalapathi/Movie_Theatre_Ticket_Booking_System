# SOFTWARE TESTING AND QUALITY ASSURANCE DOCUMENT
## Movie Theatre Ticket Booking and Box Office Analysis System
### Course Code: 25ITT33 - Software Engineering (Unit 4 Alignment)
**Institution:** Velalar College of Engineering and Technology, Erode - 12  
**Department of Information Technology**

---

## 1. Testing Strategy and Objectives

The primary objective of software testing in this project is to verify and validate that all functional requirements (FR-01 to FR-10) and non-functional requirements operate robustly, prevent data anomalies (such as overbooking and race conditions), and maintain financial consistency across ticket bookings and cancellations.

### 1.1 Testing Levels
1. **Unit Testing:** Individual validation of class methods (e.g., `reserveSeats()`, `computeRefund()`, `calculateOccupancy()`).
2. **Integration Testing:** Verification of interaction across layers: CLI Controller $\leftrightarrow$ Service Layer $\leftrightarrow$ Database DAO.
3. **System Testing:** End-to-end execution of complete workflows including concurrent multithreaded booking requests and data science analytical exports.
4. **Validation Testing:** Ensuring software complies with user expectations and business constraints specified in the SRS.

---

## 2. Test Design Techniques

### 2.1 Black-Box Testing
- **Equivalence Partitioning (EP):** Dividing input domains into valid and invalid equivalence classes.
  - *Example (Seat Selection for Screen with 10 available seats):*
    - Class 1 (Valid): $1 \le Tickets \le 10$ (Tickets accepted)
    - Class 2 (Invalid - Non-positive): $Tickets \le 0$ (Rejected with input validation error)
    - Class 3 (Invalid - Overbooking): $Tickets > 10$ (Rejected with `SeatUnavailableException`)
- **Boundary Value Analysis (BVA):** Testing at boundary limits:
  - Minimum seats: $0, 1, 2$
  - Maximum available seats $C$: $C-1, C, C+1$

### 2.2 White-Box Testing (Basis Path Testing)
Evaluating cyclomatic complexity $V(G) = E - N + 2P$ for the booking validation method to ensure all decision branches (available check, thread lock, seat decrement, status update) are exercised with 100% statement and branch coverage.

---

## 3. Comprehensive Test Suite and Test Cases

The following test matrix documents test cases executed against the application:

| Test ID | Test Module | Test Scenario & Objective | Input Data | Expected Result | Actual Result | Status |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **TC-01** | Movie Management | Add valid new movie | Title: "Avatar 3", Genre: "Sci-Fi", Lang: "English", Duration: 192 min | Movie successfully added with auto-generated ID | Movie ID 105 created; displayed in catalog | **PASS** |
| **TC-02** | Movie Management | Add movie with negative duration | Duration: -120 min | System rejects with validation error message | Input rejected with `IllegalArgumentException` | **PASS** |
| **TC-03** | Screen Management | Register screen with capacity | Screen: "Screen 3", Capacity: 150 | Screen registered with capacity 150 | Screen 3 registered; initial seats = 150 | **PASS** |
| **TC-04** | Show Scheduling | Schedule show with valid parameters | Movie ID: 101, Screen ID: 1, Date: 2026-11-20, Time: 18:30, Price: 200.00 | Show scheduled; available seats = Screen Capacity (100) | Show ID 501 created; available seats = 100 | **PASS** |
| **TC-05** | Booking Validation | Book valid number of tickets | Show ID: 501, Seats: 4, Customer: "Vaishnavi", Phone: "9876543210" | Booking confirmed; Total = 800.00; Available seats = 96 | Booking ID B101 generated; available seats updated to 96 | **PASS** |
| **TC-06** | Booking Validation | Attempt overbooking beyond capacity (BVA: $C+1$) | Show ID: 501, Available: 5, Requested: 6 | System rejects booking and throws `SeatUnavailableException` | Transaction rejected; message: "Only 5 seats available" | **PASS** |
| **TC-07** | Booking Validation | Attempt booking with zero or negative seats | Show ID: 501, Seats: 0 | Error: "Number of tickets must be at least 1" | Rejected with `InvalidBookingException` | **PASS** |
| **TC-08** | Concurrency Control | Two concurrent threads booking last available seat | Thread 1: 1 seat, Thread 2: 1 seat (Show has only 1 seat left) | Thread synchronization ensures one thread succeeds and the other receives `SeatUnavailableException` | Thread 1 confirmed booking; Thread 2 received `SeatUnavailableException`; zero overbooking | **PASS** |
| **TC-09** | Cancellation Engine | Cancel booking $>24\text{ hours}$ before show | Booking ID: B101, Total: 800.00, Lead Time: 48 hrs | 90% Refund disbursed (720.00); 10% fee (80.00); 4 seats restored | Refund: 720.00; Status: CANCELLED; Available seats restored to 100 | **PASS** |
| **TC-10** | Cancellation Engine | Cancel booking between $2\text{ and }24\text{ hours}$ | Booking ID: B102, Total: 500.00, Lead Time: 5 hrs | 70% Refund disbursed (350.00); 30% fee (150.00); seats restored | Refund: 350.00; Status: CANCELLED; seats restored | **PASS** |
| **TC-11** | Cancellation Engine | Cancel booking $<2\text{ hours}$ before show | Booking ID: B103, Total: 400.00, Lead Time: 45 min | 0% Refund (400.00 fee); seats restored | Refund: 0.00; message: "Non-refundable window"; seats restored | **PASS** |
| **TC-12** | Cancellation Engine | Attempt to cancel already cancelled booking | Booking ID: B101 (Already Cancelled) | System rejects with error: "Booking already cancelled" | Error displayed; no duplicate refund issued | **PASS** |
| **TC-13** | Revenue Calculation | Calculate net revenue with confirmed and cancelled bookings | 2 Confirmed (1500.00), 1 Cancelled (Gross: 500.00, Refund: 350.00) | Net Revenue = $(1500 + 500) - 350 = 1650.00$ | Revenue report displays exact Net Revenue = 1650.00 | **PASS** |
| **TC-14** | Reporting Engine | Generate show-wise occupancy report | Screen Capacity: 100, Booked: 75 | Occupancy Rate = 75.0% | Formatted table prints Occupancy: 75.00% | **PASS** |
| **TC-15** | Python EDA Pipeline | Retrieve dataset from DB and verify integrity | Ingest 50 show records via CSV/DB connection | Zero nulls in essential fields; 100% rows parsed correctly | 50 rows imported, cleaned, and summarized with 0 errors | **PASS** |

---

## 4. Software Quality Metrics

- **Total Test Cases Executed:** 15
- **Passed Test Cases:** 15
- **Failed Test Cases:** 0
- **Test Pass Percentage:** $\frac{15}{15} \times 100\% = 100\%$
- **Defect Density:** $0\text{ defects / KLOC}$
- **Code Coverage (Business Logic):** $> 95\%$ line coverage across service classes.
