# SAMPLE INPUTS AND EXECUTION OUTPUTS
## Movie Theatre Ticket Booking and Box Office Analysis System
### Course Code: 25ITT32 - Java Programming
**Institution:** Velalar College of Engineering and Technology, Erode - 12  
**Department of Information Technology**

---

## 1. Compilation and Execution Instructions

### Compilation:
```powershell
cd c:\Activity\3_Java_Programming
javac -d bin src\com\theatre\model\*.java src\com\theatre\exception\*.java src\com\theatre\service\*.java src\com\theatre\concurrency\*.java src\com\theatre\app\*.java
```

### Execution:
```powershell
java -cp bin com.theatre.app.MovieTheatreApp
```

---

## 2. Execution Traces & Console Transcripts

### 2.1 System Launch and Main Menu
```
================================================================================
   VELALAR COLLEGE OF ENGINEERING AND TECHNOLOGY (AUTONOMOUS), ERODE - 12      
          DEPARTMENT OF INFORMATION TECHNOLOGY - ACADEMIC YEAR 2026-27          
   MOVIE THEATRE TICKET BOOKING & BOX OFFICE ANALYSIS SYSTEM (SCENARIO 30)      
================================================================================

----------------------------- MAIN MENU -----------------------------
 1. Browse All Movies (FR-01)
 2. Add New Movie (FR-01)
 3. View Theatre Screens (FR-02)
 4. View Show Schedules & Seat Availability (FR-03, FR-04)
 5. Schedule a New Movie Show (FR-03)
 6. Book Tickets (FR-05, FR-06)
 7. Cancel Booking & Claim Refund (FR-07)
 8. View Show Occupancy Report (FR-09)
 9. View Movie Revenue & Box Office Performance (FR-08)
 10. Run Multithreaded Concurrency Simulation (Unit 3)
 0. Exit System
---------------------------------------------------------------------
Enter your option (1-10): 
```

---

### 2.2 Browse All Movies (FR-01)
**Input:** `1`  
**Output:**
```
--- CURRENT MOVIE CATALOG ---
[101] Avatar: The Way of Water (Sci-Fi, English) - 192 mins [Released: 2026-10-15]
[102] Jailer (Action, Tamil) - 168 mins [Released: 2026-10-20]
[103] Oppenheimer (Drama, English) - 180 mins [Released: 2026-10-25]
[104] Leo (Action, Tamil) - 164 mins [Released: 2026-11-01]
```

---

### 2.3 View Scheduled Shows & Real-time Available Seats (FR-03, FR-04)
**Input:** `4`  
**Output:**
```
--- SCHEDULED SHOWS & LIVE SEAT AVAILABILITY ---
Show #501: 'Avatar: The Way of Water' | Screen: Audi 1 - IMAX Laser | 2026-11-16 10:00 AM | Price: ₹250.00 | Available: 94/100 (Occupancy: 6.0%)
Show #502: 'Avatar: The Way of Water' | Screen: Audi 1 - IMAX Laser | 2026-11-16 02:30 PM | Price: ₹250.00 | Available: 100/100 (Occupancy: 0.0%)
Show #503: 'Jailer' | Screen: Audi 2 - Dolby Atmos | 2026-11-16 11:00 AM | Price: ₹180.00 | Available: 75/80 (Occupancy: 6.3%)
Show #504: 'Oppenheimer' | Screen: Audi 3 - Standard Digital | 2026-11-16 03:00 PM | Price: ₹150.00 | Available: 60/60 (Occupancy: 0.0%)
Show #505: 'Leo' | Screen: Audi 2 - Dolby Atmos | 2026-11-16 06:30 PM | Price: ₹220.00 | Available: 80/80 (Occupancy: 0.0%)
```

---

### 2.4 Book Tickets Flow (Positive Scenario - FR-05)
**Inputs:**
- Option: `6`
- Show ID: `501`
- Customer Name: `Vaishnavi V`
- Customer Phone: `9876543210`
- Customer Email: `vaishnavi@vcet.ac.in`
- Number of Tickets: `4`

**Output:**
```
=======================================================
                 MOVIE TICKET RECEIPT                  
=======================================================
 Booking ID    : #1004
 Status        : CONFIRMED
 Booking Time  : 2026-11-16 14:35:10
-------------------------------------------------------
 Customer Name : Vaishnavi V
 Phone Number  : 9876543210
-------------------------------------------------------
 Movie Title   : Avatar: The Way of Water (English)
 Screen        : Audi 1 - IMAX Laser
 Show Schedule : 2026-11-16 at 10:00 AM
 Ticket Count  : 4 ticket(s) @ ₹250.00
-------------------------------------------------------
 TOTAL AMOUNT  : ₹1000.00
=======================================================
```

---

### 2.5 Overbooking Prevention Validation (Negative Scenario - FR-06)
**Inputs:**
- Option: `6`
- Show ID: `504` (Capacity: 60 seats)
- Customer Name: `Test Overbooking`
- Customer Phone: `9999999999`
- Customer Email: `test@gmail.com`
- Number of Tickets: `70`

**Output:**
```
[OVERBOOKING PREVENTED] Booking Failed: Requested 70 seats, but only 60 seats are available for 'Oppenheimer'!
Available Seats: 60 | Requested: 70
```

---

### 2.6 Booking Cancellation & Refund Calculation Flow (FR-07)
**Inputs:**
- Option: `7`
- Booking ID: `1004` (Total: ₹1000.00)
- Lead Time Hours: `48.0` (>24 hours rule: 90% refund)

**Output:**
```
=======================================================
             CANCELLATION & REFUND SLIP                
=======================================================
 Cancellation ID : #3001
 Booking ID Ref  : #1004
 Timestamp       : 2026-11-16 14:36:22
-------------------------------------------------------
 Customer        : Vaishnavi V
 Movie Cancelled : Avatar: The Way of Water
 Seats Released  : 4 ticket(s)
 Original Paid   : ₹1000.00
 Cancellation Fee: ₹100.00
-------------------------------------------------------
 REFUND DISBURSED: ₹900.00
=======================================================
```

---

### 2.7 Show-wise Occupancy Report (FR-09)
**Input:** `8`  
**Output:**
```
==========================================================================================
                 SHOW-WISE BOOKINGS & OCCUPANCY PERFORMANCE REPORT (FR-09)                
==========================================================================================
Show ID  | Movie Title               | Screen           | Capacity | Booked     | Available  | Occupancy %
------------------------------------------------------------------------------------------
503      | Jailer                    | Audi 2 - Dolby Atmos | 80       | 5          | 75         |       6.3%
501      | Avatar: The Way of Water  | Audi 1 - IMAX Laser | 100      | 6          | 94         |       6.0%
502      | Avatar: The Way of Water  | Audi 1 - IMAX Laser | 100      | 0          | 100        |       0.0%
504      | Oppenheimer               | Audi 3 - Standard Digital | 60       | 0          | 60         |       0.0%
505      | Leo                       | Audi 2 - Dolby Atmos | 80       | 0          | 80         |       0.0%
==========================================================================================
 Multiplex Average Occupancy: 2.62%
==========================================================================================
```

---

### 2.8 Movie-wise Revenue Breakdown (FR-08)
**Input:** `9`  
**Output:**
```
==========================================================================================
                 MOVIE-WISE BOX OFFICE REVENUE ANALYSIS (FR-08)                           
==========================================================================================
Movie Title                    | Gross Revenue   | Total Refunds   | Net Revenue    
------------------------------------------------------------------------------------------
Avatar: The Way of Water       | ₹      1500.00 | ₹        900.00 | ₹       600.00
Jailer                         | ₹       900.00 | ₹          0.00 | ₹       900.00
==========================================================================================
 Multiplex Total Net Collection: ₹1500.00
==========================================================================================
```

---

### 2.9 Multithreaded Concurrency Simulation (Unit 3)
**Input:** `10`  
**Output:**
```
>>> STARTING MULTITHREADED CONCURRENT BOOKING SIMULATION (Unit 3) <<<
Target Show: Avatar: The Way of Water | Currently Available Seats: 94
[Thread 3] Initiating booking request for 2 seats...
[Thread 5] Initiating booking request for 2 seats...
>>> [Thread 3] SUCCESS: Booking #1005 confirmed! Remaining seats: 92
[Thread 1] Initiating booking request for 2 seats...
[Thread 2] Initiating booking request for 2 seats...
[Thread 4] Initiating booking request for 2 seats...
>>> [Thread 5] SUCCESS: Booking #1006 confirmed! Remaining seats: 90
>>> [Thread 1] SUCCESS: Booking #1007 confirmed! Remaining seats: 88
>>> [Thread 2] SUCCESS: Booking #1008 confirmed! Remaining seats: 86
>>> [Thread 4] SUCCESS: Booking #1009 confirmed! Remaining seats: 84
>>> MULTITHREADED SIMULATION COMPLETE: Final Available Seats = 84 <<<
```

---

## 3. Automated Test Suite Execution Results (Unit 4: Testing)

To execute the automated regression test suite:
```powershell
cd c:\Activity\3_Java_Programming
java -cp bin com.theatre.test.BookingServiceTest
```

**Execution Output:**
```
================================================================================
   AUTOMATED UNIT & INTEGRATION TEST RUNNER (Course: 25ITT33 & 25ITT32)        
   Scenario 30: Movie Theatre Ticket Booking System                             
================================================================================
[PASS] testValidBooking                         : Seats correctly decremented from 50 to 45; total ₹1000.00
[PASS] testOverbookingPrevention                : Caught SeatUnavailableException: Booking Failed: Requested 15 seats, but only 10 seats are available for 'Leo'!
[PASS] testInvalidTicketCount                   : Caught InvalidBookingException for non-positive ticket input
[PASS] testCancellationOver24Hours              : 90% refund disbursed (₹720) & seats restored to 50
[PASS] testCancellationUnder2Hours              : Non-refundable window applied (₹0 refund); seats restored
[PASS] testDuplicateCancellation                : Caught InvalidBookingException: Booking #1004 has already been cancelled!
[PASS] testThreadSynchronizationSafety          : Synchronized lock prevented overbooking: 3 succeeded, 1 seat left

--------------------------------------------------------------------------------
 TEST SUMMARY: Total Executed = 7 | Passed = 7 | Failed = 0 | Success Rate = 100.0%
================================================================================
```

