# DATABASE NORMALIZATION SPECIFICATION
## Step-by-Step Normalization (1NF, 2NF, 3NF, BCNF)
### Course Code: 25ITT31 - Database Management Systems (Unit 3 Alignment)
**Institution:** Velalar College of Engineering and Technology, Erode - 12  
**Department of Information Technology**

---

## 1. Fundamentals of Relational Normalization

Database Normalization is the formal process of organizing attributes and relations in a database to:
1. Eliminate data redundancy.
2. Prevent insertion, update, and deletion anomalies.
3. Enforce data integrity through functional dependencies (FDs).
4. Ensure lossless join decomposition and dependency preservation.

---

## 2. Functional Dependencies & Armstrong's Axioms

### 2.1 Set of Functional Dependencies ($F$)
For the Movie Theatre domain:
- $FD_1: \text{movie\_id} \to \{\text{title, genre, language, duration\_minutes, release\_date}\}$
- $FD_2: \text{screen\_id} \to \{\text{screen\_name, seating\_capacity}\}$
- $FD_3: \text{show\_id} \to \{\text{movie\_id, screen\_id, show\_date, show\_time, ticket\_price, available\_seats}\}$
- $FD_4: \text{customer\_id} \to \{\text{customer\_name, phone, email}\}$
- $FD_5: \text{booking\_id} \to \{\text{customer\_id, show\_id, number\_of\_tickets, total\_amount, booking\_date, booking\_status}\}$
- $FD_6: \text{cancellation\_id} \to \{\text{booking\_id, cancellation\_date, refund\_amount, cancellation\_fee}\}$
- $FD_7: \text{payment\_id} \to \{\text{booking\_id, payment\_mode, amount, payment\_status, transaction\_time}\}$

### 2.2 Application of Armstrong's Axioms
- **Reflexivity:** If $Y \subseteq X$, then $X \to Y$. (e.g., $\{\text{show\_id, movie\_id}\} \to \text{movie\_id}$)
- **Augmentation:** If $X \to Y$, then $XZ \to YZ$.
- **Transitivity:** If $X \to Y$ and $Y \to Z$, then $X \to Z$.
  - Example: $\text{booking\_id} \to \text{show\_id}$ and $\text{show\_id} \to \text{screen\_id} \implies \text{booking\_id} \to \text{screen\_id}$ (Transitive dependency resolved in 3NF).

---

## 3. Step-by-Step Normalization Process

### 3.1 Unnormalized Form (UNF)
Consider a flat, unnormalized spreadsheet relation commonly found in manual theatre logs:

$$\text{THEATRE\_MASTER}(\underline{\text{BookingID}}, \text{CustomerName}, \text{Phone}, \text{MovieTitle}, \text{Genre}, \text{ScreenName}, \text{Capacity}, \text{ShowTime}, \text{Price}, \text{SeatsBooked}, \text{Total}, \text{SeatsList})$$

#### Anomalies Present in UNF:
1. **Insertion Anomaly:** We cannot register a new screen or movie into the database without first booking a ticket.
2. **Update Anomaly:** If a movie title or ticket price changes, hundreds of duplicate booking rows must be updated; any missed row creates data inconsistency.
3. **Deletion Anomaly:** If the only booking for a movie show is cancelled and deleted, all record of the movie's details is permanently lost.

---

### 3.2 First Normal Form (1NF)
> **Definition:** A relation $R$ is in 1NF if and only if all attribute domains contain only atomic (indivisible) values, and there are no repeating groups or multivalued attributes.

- In UNF, `SeatsList` contains comma-separated values (e.g., `"A1, A2, A3"`).
- **Transformation to 1NF:** Remove multi-valued attributes and represent each atomic ticket item individually or by an integer count (`number_of_tickets`) alongside an explicit composite key.

$$\text{1NF Relation: } R_1(\underline{\text{BookingID, SeatNo}}, \text{CustID, CustName, CustPhone, MovieID, Title, Genre, ScreenID, ScreenName, Capacity, ShowID, ShowTime, Price, Total})$$

---

### 3.3 Second Normal Form (2NF)
> **Definition:** A relation is in 2NF if it is in 1NF and every non-prime attribute is fully functionally dependent on the entire candidate key (no partial dependencies: $X \to Y$ where $X \subset \text{Candidate Key}$).

- **Candidate Key:** $\{\text{BookingID, SeatNo}\}$
- **Partial Dependencies identified:**
  - $\text{BookingID} \to \text{CustID, CustName, CustPhone, ShowID, ShowTime, Price, Total}$
  - Here, customer and show details depend solely on `BookingID`, not on `SeatNo`.
- **Decomposition into 2NF Relations:**
  1. `BOOKING_SEAT` ($\underline{\text{BookingID, SeatNo}}$)
  2. `BOOKING_RECORD` ($\underline{\text{BookingID}}, \text{CustID, CustName, CustPhone, ShowID, ShowTime, MovieID, Title, ScreenID, ScreenName, Capacity, Price, Total}$)

---

### 3.4 Third Normal Form (3NF)
> **Definition:** A relation is in 3NF if it is in 2NF and there are no transitive dependencies of non-prime attributes on the primary key ($X \to Y$ and $Y \to Z$ where $Y$ is not a superkey).

- In `BOOKING_RECORD`:
  - $\text{BookingID} \to \text{CustID}$ and $\text{CustID} \to \{\text{CustName, CustPhone}\}$ ($\text{Transitive}$)
  - $\text{BookingID} \to \text{ShowID}$ and $\text{ShowID} \to \{\text{MovieID, ScreenID, ShowTime, Price}\}$ ($\text{Transitive}$)
  - $\text{ShowID} \to \text{MovieID}$ and $\text{MovieID} \to \{\text{Title, Genre}\}$ ($\text{Transitive}$)
  - $\text{ShowID} \to \text{ScreenID}$ and $\text{ScreenID} \to \{\text{ScreenName, Capacity}\}$ ($\text{Transitive}$)

- **Decomposition into 3NF Relations:**
  1. **`Customer`** ($\underline{\text{customer\_id}}$, customer\_name, phone, email)
  2. **`Movie`** ($\underline{\text{movie\_id}}$, title, genre, language, duration\_minutes, release\_date)
  3. **`Screen`** ($\underline{\text{screen\_id}}$, screen\_name, seating\_capacity)
  4. **`ShowSchedule`** ($\underline{\text{show\_id}}$, $\text{movie\_id}^*$, $\text{screen\_id}^*$, show\_date, show\_time, ticket\_price, available\_seats)
  5. **`Booking`** ($\underline{\text{booking\_id}}$, $\text{customer\_id}^*$, $\text{show\_id}^*$, number\_of\_tickets, total\_amount, booking\_date, booking\_status)
  6. **`Cancellation`** ($\underline{\text{cancellation\_id}}$, $\text{booking\_id}^*$, cancellation\_date, refund\_amount, cancellation\_fee)
  7. **`Payment`** ($\underline{\text{payment\_id}}$, $\text{booking\_id}^*$, payment\_mode, amount, payment\_status, transaction\_time)

---

### 3.5 Boyce-Codd Normal Form (BCNF) Verification
> **Definition:** A relation $R$ is in BCNF if for every non-trivial functional dependency $X \to Y$, $X$ is a superkey of $R$.

Let us test all decomposed relations against the BCNF rule:
1. `Customer`: Determinant is `customer_id`, which is the candidate key $\implies$ **Satisfies BCNF**.
2. `Movie`: Determinant is `movie_id`, which is the candidate key $\implies$ **Satisfies BCNF**.
3. `Screen`: Determinant is `screen_id`, which is the candidate key $\implies$ **Satisfies BCNF**.
4. `ShowSchedule`: Determinant is `show_id`, which is the candidate key $\implies$ **Satisfies BCNF**.
5. `Booking`: Determinant is `booking_id`, which is the candidate key $\implies$ **Satisfies BCNF**.
6. `Cancellation`: Determinant is `cancellation_id`, which is the candidate key $\implies$ **Satisfies BCNF**.
7. `Payment`: Determinant is `payment_id`, which is the candidate key $\implies$ **Satisfies BCNF**.

**Conclusion:** The schema is rigorously normalized up to **BCNF**.

---

## 4. Properties of the Decomposition

1. **Lossless Join Decomposition:** Guaranteed because for each decomposition $R = R_1 \cup R_2$, the intersection $R_1 \cap R_2$ forms a candidate key of at least one of the decomposed schemas.
   $$\text{ShowSchedule} \cap \text{Movie} = \{\text{movie\_id}\} \ (\text{Candidate key of Movie})$$
2. **Dependency Preservation:** Every functional dependency in the original set $F$ can be verified within individual relations without requiring joins across tables.

---

## 5. Denormalization Strategy for Analytics Subsystem
While the transactional core is kept strictly in BCNF to prevent anomalies during concurrent bookings, analytical reporting and Python data science routines ingest a materialized flat view (`vw_theatre_analytics_export`) to minimize costly multi-table joins during complex statistical and visualization computations.
