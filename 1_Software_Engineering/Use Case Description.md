# USE CASE SPECIFICATIONS AND ACTOR DESCRIPTIONS
## Movie Theatre Ticket Booking and Box Office Analysis System
### Course Code: 25ITT33 - Software Engineering (Unit 2 & 3 Alignment)
**Institution:** Velalar College of Engineering and Technology (Autonomous), Erode - 12  
**Department:** Information Technology | **Class:** 25IT3A & 25IT3B  
**Scenario:** 30 (Register Nos: 30, 60, 90, 120)

---

## 1. ACTORS CATALOG

In the Movie Theatre Ticket Booking and Box Office Analysis System, actors represent human roles or external systems that interact with the application boundary.

| Actor Name | Type | Description & Responsibilities |
| :--- | :--- | :--- |
| **Customer** | Primary Human Actor | An end-user who explores movie catalogs, selects show schedules, verifies available seats, reserves tickets, conducts online payment, receives ticket receipts, and submits booking cancellation requests for applicable refunds. |
| **Theatre Administrator** | Primary Human Actor | An operational manager responsible for maintaining movie information (title, genre, language, duration), configuring physical screen capacities, assigning show schedules, setting base ticket prices, and reviewing operational occupancy reports. |
| **Box Office Analyst** | Secondary Human Actor | A data scientist or business intelligence specialist who extracts historical booking transactions, runs the Python data science pipeline, performs exploratory data analysis (EDA), computes $Z$-scores, and evaluates box office visualizations. |
| **Payment Gateway** | External System Actor | A simulated payment processing intermediary that validates payment credentials (UPI, Credit/Debit Card, NetBanking) and authorizes financial transactions. |
| **Relational Database** | Supporting System Actor | MySQL database engine enforcing schema integrity, ACID transaction boundaries, automated triggers, and materializing analytical views. |

---

## 2. STEP-BY-STEP USE CASE DESCRIPTIONS

---

### UC-01: Browse Movies & Shows
- **Use Case ID:** UC-01
- **Use Case Name:** Browse Movies & Shows
- **Primary Actor:** Customer, Theatre Administrator
- **Preconditions:** The application is running, and at least one movie/show schedule exists in the system.
- **Trigger:** The actor selects menu option 1 or 4 from the main interface.
- **Normal Flow of Events:**
  1. Actor submits a request to view available movies or upcoming show schedules.
  2. System queries the in-memory cache and `show_schedule` database view.
  3. System retrieves active movie titles, genres, languages, run durations, scheduled dates, and screen names.
  4. System formats and displays the catalog in a structured tabular view.
  5. Use case ends successfully.
- **Alternative Flows:**
  - *3a. No Active Shows:* If no shows are currently scheduled, the system displays an informational message: `"No active shows found. Please contact theatre administration."`
- **Postconditions:** The actor is informed of all current movies and showtimes without altering system state.

---

### UC-02: Check Seat Availability
- **Use Case ID:** UC-02
- **Use Case Name:** Check Seat Availability
- **Primary Actor:** Customer
- **Preconditions:** The selected show exists in the database.
- **Trigger:** The customer enters a Show ID to inspect seat status.
- **Normal Flow of Events:**
  1. Customer specifies the `show_id` of interest.
  2. System looks up the `ShowSchedule` object.
  3. System dynamically computes remaining seats:
     $$\text{Available Seats} = \text{Screen Seating Capacity} - \text{Booked Seats}$$
  4. System displays the screen name, total capacity, booked count, remaining seats, and live occupancy percentage.
  5. Use case ends.
- **Exception Flow:**
  - *2a. Invalid Show ID:* If the Show ID does not exist, the system raises `ShowNotFoundException` and prompts for re-entry.
- **Postconditions:** Live seat counts are presented to the customer.

---

### UC-03: Book Tickets
- **Use Case ID:** UC-03
- **Use Case Name:** Book Tickets
- **Primary Actor:** Customer
- **Secondary Actor:** Payment Gateway, Relational Database
- **Preconditions:**
  1. The customer has browsed available shows (`UC-01`).
  2. The selected show has remaining available seats $> 0$.
- **Trigger:** Customer selects menu option 6 (*"Book Tickets"*).
- **Normal Flow of Events:**
  1. Customer inputs the target `show_id`.
  2. System retrieves and displays show details (movie title, auditorium screen, ticket price, and available seats).
  3. Customer enters customer name, phone number, email address, and desired ticket count ($N$).
  4. System invokes **`UC-04: Validate Seating Capacity`** to verify that $N \le \text{Available Seats}$.
  5. System enters a synchronized thread-safe block and reserves $N$ seats.
  6. System calculates the gross payable amount:
     $$\text{Total Amount} = N \times \text{Ticket Price}$$
  7. System invokes **`UC-05: Process Ticket Payment`** for billing authorization.
  8. Upon payment confirmation, the system generates an auto-incremented `booking_id`.
  9. System persists the booking in the relational database (`INSERT INTO booking`).
  10. System constructs a formatted ticket receipt using `StringBuilder` containing Booking ID, movie title, screen name, timestamp, and total cost.
  11. System presents the ticket receipt on the console and updates the live occupancy metric.
  12. Use case terminates successfully.
- **Alternative & Exception Flows:**
  - *4a. Insufficient Seats (Overbooking Attempt):* If $N > \text{Available Seats}$, `UC-04` raises `SeatUnavailableException`. The transaction aborts, no seats are decremented, and an error message displaying remaining seats is shown.
  - *3a. Invalid Ticket Volume:* If $N \le 0$, system raises `InvalidBookingException` with message: `"Ticket count must be at least 1."`
  - *7a. Payment Failure:* If payment is declined, the reserved seats are released back to the show inventory, and the booking transaction rolls back cleanly.
- **Postconditions:**
  - Available seats in the show are decremented by $N$.
  - A confirmed booking record is permanently stored with status `CONFIRMED`.
  - Customer receives a verifiable ticket receipt.

---

### UC-04: Validate Seating Capacity (`<<include>>`)
- **Use Case ID:** UC-04
- **Use Case Name:** Validate Seating Capacity
- **Primary Actor:** System (invoked by `UC-03`)
- **Preconditions:** Customer has requested $N$ tickets for a specific show.
- **Normal Flow of Events:**
  1. System checks the `availableSeats` attribute of `ShowSchedule`.
  2. System verifies condition: $N \le \text{availableSeats}$.
  3. System confirms valid capacity and returns authorization to proceed.
- **Exception Flow:**
  - *2a. Capacity Exceeded:* System throws `SeatUnavailableException(requested, available)`. Processing halts immediately.
- **Postconditions:** Guarantees zero overbooking across concurrent reservation attempts.

---

### UC-05: Process Ticket Payment (`<<include>>`)
- **Use Case ID:** UC-05
- **Use Case Name:** Process Ticket Payment
- **Primary Actor:** Customer, Payment Gateway
- **Preconditions:** Ticket validation succeeded; total amount is calculated.
- **Normal Flow of Events:**
  1. System presents total payable amount (₹).
  2. Customer selects payment mode (UPI / Card / NetBanking / Cash).
  3. System submits transaction payload to Payment Gateway.
  4. Payment Gateway returns `SUCCESS` with a transaction reference timestamp.
  5. System generates a `Payment` entity linked to the `booking_id`.
- **Postconditions:** Financial settlement recorded; payment status set to `SUCCESS`.

---

### UC-06: Cancel Booking & Claim Refund
- **Use Case ID:** UC-06
- **Use Case Name:** Cancel Booking & Claim Refund
- **Primary Actor:** Customer
- **Secondary Actor:** Relational Database
- **Preconditions:** Customer possesses a valid `booking_id` with status `CONFIRMED`.
- **Trigger:** Customer selects menu option 7 (*"Cancel Booking & Claim Refund"*).
- **Normal Flow of Events:**
  1. Customer inputs their `booking_id`.
  2. System searches active bookings; verifies that status is `CONFIRMED`.
  3. Customer inputs the remaining lead time in hours ($T$) before show start time.
  4. System executes the **Lead-Time Refund Decision Engine**:
     - **Tier 1 ($T \ge 24\text{ hours}$):** Refund Percentage = **90%** (10% cancellation charge).
     - **Tier 2 ($2\text{ hours} \le T < 24\text{ hours}$):** Refund Percentage = **70%** (30% cancellation charge).
     - **Tier 3 ($T < 2\text{ hours}$):** Refund Percentage = **0%** (100% cancellation charge; non-refundable window).
  5. System computes:
     $$\text{Refund Amount} = \text{Total Amount} \times \left(\frac{\text{Refund Percentage}}{100}\right)$$
     $$\text{Cancellation Fee} = \text{Total Amount} - \text{Refund Amount}$$
  6. System enters a synchronized block and restores the booked tickets back to `ShowSchedule.availableSeats`:
     $$\text{availableSeats} \leftarrow \text{availableSeats} + N$$
  7. System updates booking status to `CANCELLED`.
  8. System records the transaction in `cancellation` table.
  9. System formats and outputs the official Cancellation & Refund Slip detailing customer name, movie title, seats released, original amount, fee deducted, and net refund disbursed.
  10. Use case terminates successfully.
- **Alternative & Exception Flows:**
  - *2a. Non-existent Booking ID:* System throws `InvalidBookingException: "Booking ID not found."`
  - *2b. Already Cancelled:* If booking status is already `CANCELLED`, system raises `InvalidBookingException: "Booking #ID has already been cancelled!"` preventing double refunds.
- **Postconditions:**
  - Show inventory restored by $N$ seats.
  - Booking status updated to `CANCELLED`.
  - Cancellation record persisted with audited refund amount.

---

### UC-07: Manage Movie Details
- **Use Case ID:** UC-07
- **Use Case Name:** Manage Movie Details
- **Primary Actor:** Theatre Administrator
- **Preconditions:** Administrator has authenticated access to management options.
- **Trigger:** Administrator selects option 2 (*"Add New Movie"*).
- **Normal Flow of Events:**
  1. Administrator enters Movie ID, title, genre (Action, Sci-Fi, Drama), audio language, duration in minutes, and release date.
  2. System validates that duration $> 0$ and title is non-empty.
  3. System instantiates a `Movie` object.
  4. System adds the movie to the catalog and executes `MovieDAO.insertMovie()`.
  5. System confirms successful addition.
- **Exception Flow:**
  - *2a. Negative Duration:* System rejects input with `IllegalArgumentException: "Duration must be strictly positive!"`
- **Postconditions:** New movie is registered and available for scheduling.

---

### UC-08: Configure Theatre Screens
- **Use Case ID:** UC-08
- **Use Case Name:** Configure Theatre Screens
- **Primary Actor:** Theatre Administrator
- **Normal Flow of Events:**
  1. Administrator specifies Screen ID, screen name (e.g., Audi 1 - IMAX), and seating capacity.
  2. System validates capacity $> 0$.
  3. System registers the `Screen` instance in the database.
- **Postconditions:** Auditorium screen configuration is saved with physical seating limits.

---

### UC-09: Schedule Movie Shows
- **Use Case ID:** UC-09
- **Use Case Name:** Schedule Movie Shows
- **Primary Actor:** Theatre Administrator
- **Preconditions:** At least one movie and one screen exist.
- **Trigger:** Administrator selects option 5 (*"Schedule a New Movie Show"*).
- **Normal Flow of Events:**
  1. System displays registered movies and screens.
  2. Administrator selects `movie_id`, `screen_id`, show date (YYYY-MM-DD), show time (HH:MM), and base ticket price (₹).
  3. System validates that no other movie is scheduled on the same screen at the identical date and time.
  4. System initializes `availableSeats` to equal the screen's `seatingCapacity`.
  5. System saves the `ShowSchedule` record.
  6. System confirms show creation.
- **Postconditions:** Show schedule is active and open for customer bookings.

---

### UC-10: View Operational Reports
- **Use Case ID:** UC-10
- **Use Case Name:** View Operational Reports
- **Primary Actor:** Theatre Administrator, Box Office Analyst
- **Trigger:** User selects option 8 (*Show Occupancy*) or option 9 (*Revenue Analysis*).
- **Normal Flow of Events:**
  1. System invokes `ReportServiceImpl`.
  2. Using Java Stream API, system filters and sorts shows by occupancy percentage:
     $$\text{Occupancy \%} = \left(\frac{\text{Booked Seats}}{\text{Total Capacity}}\right) \times 100$$
  3. System aggregates movie gross revenue and subtracts cumulative refunds to compute net revenue:
     $$\text{Net Revenue} = \sum \text{Gross Sales} - \sum \text{Refunds}$$
  4. System formats output in aligned ASCII tables.
- **Postconditions:** Operational management reports displayed on console.

---

### UC-11: Analyze Box Office Performance (Python EDA)
- **Use Case ID:** UC-11
- **Use Case Name:** Analyze Box Office Performance
- **Primary Actor:** Box Office Analyst
- **Preconditions:** Booking dataset has been exported from MySQL database into `theatre_bookings.csv`.
- **Trigger:** Analyst runs `box_office_analysis.py` or opens `box_office_analysis.ipynb`.
- **Normal Flow of Events:**
  1. Python script ingests `theatre_bookings.csv` via Pandas.
  2. Script executes data cleaning: fills missing values, audits duplicate booking IDs, and casts datetime columns.
  3. Script performs IQR outlier analysis and computes standardized $Z$-scores.
  4. Script generates multi-variable aggregations and two-way pivot tables (Screen vs. Genre).
  5. Script invokes Matplotlib and Seaborn to generate 6 publication-ready charts (Bar, Donut, Boxplot, Heatmap, Regplot).
  6. Script exports vector visualizations into `visualizations/` and refreshes `interactive_dashboard.html`.
- **Postconditions:** Statistical insights and interactive visualizations generated for executive decision-making.
