# Java Program Explanation

**Project:** Scenario 30 – Movie Theatre Ticket Booking and Box Office Analysis System  
**Institution:** Velalar College of Engineering and Technology (Autonomous), Erode – 12  
**Department:** Information Technology | **Academic Year:** 2026-27 (ODD)  
**Class:** 25IT3A & 25IT3B | **Register Numbers:** 30, 60, 90, 120

---

## 1. Architecture Overview

The Java application follows a **three-layer architecture**:

```
┌─────────────────────────────────────────┐
│   Presentation Layer (app/)             │  MovieTheatreApp.java – Console UI
├─────────────────────────────────────────┤
│   Business Logic Layer (service/)       │  BookingService, CancellationService,
│                                         │  ReportService
├─────────────────────────────────────────┤
│   Data / Model Layer (model/, dao/)     │  Movie, Screen, ShowSchedule, Customer,
│                                         │  Booking, Cancellation, BookingDAO
└─────────────────────────────────────────┘
```

Supporting packages: `exception/` (custom errors), `concurrency/` (thread-safe simulation), `test/` (automated tests).

---

## 2. Package and Class Reference

### 2.1 `com.theatre.model` – Domain Model Classes

These classes represent the real-world entities of the cinema system.

#### `Movie.java`
```java
public class Movie {
    private String movieId;
    private String title;
    private String genre;
    private String language;
    private int durationMinutes;
    private String rating;
    private LocalDate releaseDate;
    // Constructors, getters, setters, toString()
}
```
**OOP Concepts:** Encapsulation (private fields, public getters/setters), immutable `movieId`.

---

#### `Screen.java`
```java
public class Screen {
    private String screenId;
    private String screenName;
    private int totalSeats;
    private int regularSeats;
    private int premiumSeats;
    private int vipSeats;
    // Constructors, getters, setters
}
```
**OOP Concepts:** Encapsulation; `totalSeats` validated in constructor.

---

#### `ShowSchedule.java`
```java
public class ShowSchedule {
    private String showId;
    private Movie movie;        // Composition – ShowSchedule HAS-A Movie
    private Screen screen;      // Composition – ShowSchedule HAS-A Screen
    private LocalDateTime showDateTime;
    private int availableSeats;
    private double priceRegular;
    private double pricePremium;
    private double priceVip;
    private ShowStatus status;  // Enum: SCHEDULED, RUNNING, COMPLETED, CANCELLED
    // Constructors, getters, setters
}
```
**OOP Concepts:** Composition (aggregates `Movie` and `Screen`); ENUM for status.

---

#### `Customer.java`
```java
public class Customer extends Person {
    private String customerId;
    private String email;
    private String phone;
    private int loyaltyPoints;
    // Constructors, getters, setters
}
```
**OOP Concepts:** **Inheritance** – `Customer` extends `Person` (IS-A relationship). `Person` holds `fullName`; `Customer` adds customer-specific fields.

---

#### `Booking.java`
```java
public class Booking {
    private String bookingId;
    private Customer customer;   // Composition
    private ShowSchedule show;   // Composition
    private SeatType seatType;   // Enum: REGULAR, PREMIUM, VIP
    private int numTickets;
    private List<String> seatNumbers;
    private double totalAmount;
    private String paymentMethod;
    private BookingStatus status; // Enum: CONFIRMED, CANCELLED, NO_SHOW
    private LocalDateTime bookedAt;
    // Constructors, getters, setters
}
```
**OOP Concepts:** Composition; Enums; Java Collections (`List<String>`).

---

#### `Cancellation.java`
```java
public class Cancellation {
    private String cancellationId;
    private Booking booking;      // Composition
    private LocalDateTime cancelledAt;
    private double hoursBeforeShow;
    private int refundTier;       // 1, 2, or 3
    private double refundPercentage;
    private double refundAmount;
    // Constructors, getters, setters
}
```

---

### 2.2 `com.theatre.exception` – Custom Exception Classes

Three checked/unchecked exceptions handle domain-specific error conditions:

#### `OverbookingException.java`
```java
public class OverbookingException extends RuntimeException {
    private final int requested;
    private final int available;

    public OverbookingException(int requested, int available) {
        super("Cannot book " + requested + " seats; only " + available + " available.");
        this.requested  = requested;
        this.available  = available;
    }
    public int getRequested() { return requested; }
    public int getAvailable() { return available; }
}
```
**Thrown by:** `BookingService.reserveSeats()` when `numTickets > show.getAvailableSeats()`.

#### `InvalidBookingException.java`
```java
public class InvalidBookingException extends RuntimeException {
    public InvalidBookingException(String message) { super(message); }
}
```
**Thrown by:** `BookingService` when quantity ≤ 0 or customer data is null.

#### `ShowNotFoundException.java`
```java
public class ShowNotFoundException extends RuntimeException {
    public ShowNotFoundException(String showId) {
        super("Show not found: " + showId);
    }
}
```
**Thrown by:** `BookingService.getShow()` lookup method.

---

### 2.3 `com.theatre.service` – Business Logic Layer

#### `BookingService.java` – Core Booking Engine

```java
public class BookingService {
    private final Map<String, ShowSchedule> shows;   // In-memory show registry
    private final Map<String, Booking>      bookings; // In-memory booking store
    private final ReentrantLock             lock;     // Thread-safety

    // Thread-safe seat reservation
    public Booking reserveSeats(String showId, Customer customer,
                                int qty, SeatType type) {
        lock.lock();
        try {
            ShowSchedule show = getShow(showId);         // throws ShowNotFoundException
            if (qty <= 0) throw new InvalidBookingException("...");
            if (qty > show.getAvailableSeats())
                throw new OverbookingException(qty, show.getAvailableSeats());

            show.setAvailableSeats(show.getAvailableSeats() - qty); // Atomic decrement
            double price  = getPrice(show, type) * qty;
            String bookId = generateBookingId();
            Booking b     = new Booking(bookId, customer, show, type, qty, price);
            bookings.put(bookId, b);
            return b;
        } finally {
            lock.unlock(); // Always released – prevents deadlock
        }
    }
}
```

**OOP Concepts Used:**
- **Encapsulation** – all fields private; exposed via methods only
- **Abstraction** – `reserveSeats()` hides lock mechanics from caller
- **Exception Handling** – three custom exceptions for distinct failure modes
- **Java Collections** – `HashMap` for O(1) lookup of shows and bookings
- **Concurrency** – `ReentrantLock` with try-finally pattern

---

#### `CancellationService.java` – Tiered Refund Engine

```java
public class Cancellation cancel(String bookingId, String reason) {
    Booking b = getBooking(bookingId);
    Duration gap = Duration.between(LocalDateTime.now(), b.getShow().getShowDateTime());
    double hours = gap.toMinutes() / 60.0;

    int    tier;
    double pct;
    if      (hours >= 24) { tier = 1; pct = 90.0; }
    else if (hours >= 12) { tier = 2; pct = 50.0; }
    else                  { tier = 3; pct =  0.0; }

    double refund = b.getTotalAmount() * pct / 100.0;
    // Create Cancellation, update Booking status, release seats
}
```

**Key Java APIs:** `java.time.LocalDateTime`, `java.time.Duration`, conditional refund logic.

---

#### `ReportService.java` – Box Office Analytics

```java
public void printOccupancyReport(List<ShowSchedule> shows, List<Booking> bookings) {
    shows.stream()
         .map(show -> {
             long booked  = bookings.stream()
                                    .filter(b -> b.getShow().equals(show))
                                    .mapToInt(Booking::getNumTickets).sum();
             double occ   = (booked * 100.0) / show.getScreen().getTotalSeats();
             double rev   = bookings.stream()
                                    .filter(b -> b.getShow().equals(show))
                                    .mapToDouble(Booking::getTotalAmount).sum();
             return new ShowReport(show, booked, occ, rev);
         })
         .sorted(Comparator.comparingDouble(ShowReport::getOccupancy).reversed())
         .forEach(System.out::println);
}
```

**Java 8+ Concepts:** Stream API, `filter()`, `map()`, `mapToDouble()`, `sum()`, `Comparator`, method references (`::`)

---

### 2.4 `com.theatre.concurrency` – Thread-Safe Simulation

#### `ConcurrentBookingSimulator.java`

```java
public class ConcurrentBookingSimulator {
    public void simulate(ShowSchedule show, int numThreads) throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(numThreads);
        for (int i = 0; i < numThreads; i++) {
            final int tid = i;
            pool.submit(() -> {
                try {
                    Booking b = bookingService.reserveSeats(show.getShowId(),
                                    testCustomers[tid], 2, SeatType.REGULAR);
                    System.out.println("Thread-" + tid + " BOOKED: " + b.getBookingId());
                } catch (OverbookingException e) {
                    System.out.println("Thread-" + tid + " REJECTED: " + e.getMessage());
                }
            });
        }
        pool.shutdown();
        pool.awaitTermination(10, TimeUnit.SECONDS);
    }
}
```

**Concurrency Concepts:** `ExecutorService`, `Executors.newFixedThreadPool()`, `submit()`, `awaitTermination()`. The `ReentrantLock` in `BookingService` ensures zero race conditions.

---

### 2.5 `com.theatre.test` – Automated Test Suite

#### `BookingServiceTest.java`

7 test methods, each following the **Arrange → Act → Assert** pattern:

```java
public void testOverbookingPrevention() {
    // Arrange
    ShowSchedule show = createShow("SH-TEST", 2); // Only 2 seats

    // Act & Assert
    try {
        bookingService.reserveSeats("SH-TEST", customer, 5, SeatType.REGULAR);
        fail("Expected OverbookingException was not thrown");
    } catch (OverbookingException e) {
        assert e.getAvailable() == 2 : "Available seats mismatch";
        assert e.getRequested() == 5 : "Requested seats mismatch";
        System.out.println("✓  PASS  TC-02  testOverbookingPrevention");
    }
}
```

---

## 3. OOP Concepts Summary Table

| OOP Concept | Where Applied | Example |
|-------------|--------------|---------|
| **Encapsulation** | All model classes | Private fields + public getters/setters |
| **Inheritance** | `Customer extends Person` | Customer IS-A Person |
| **Polymorphism** | `toString()` overrides | Each model prints its own representation |
| **Abstraction** | Service layer interfaces | `BookingService` hides lock complexity |
| **Composition** | `Booking`, `ShowSchedule` | Booking HAS-A Customer, HAS-A ShowSchedule |
| **Exception Handling** | `BookingService`, `CancellationService` | 3 custom runtime exceptions |
| **Generics** | Collections throughout | `Map<String, Booking>`, `List<String>` |
| **Enums** | `SeatType`, `BookingStatus` | Type-safe status values |
| **Stream API** | `ReportService` | filter, map, sum, sorted |
| **Concurrency** | `BookingService`, `Simulator` | ReentrantLock, ExecutorService |
| **java.time API** | `CancellationService` | LocalDateTime, Duration for refund calculation |
| **StringBuilder** | Receipt/slip printing | Efficient multi-line string building |

---

## 4. Key Design Patterns Used

| Pattern | Implementation |
|---------|---------------|
| **Singleton** | `BookingService` instance shared across all sessions |
| **Factory Method** | `generateBookingId()` creates unique formatted IDs |
| **Strategy** | Refund tier selection (3 strategies based on hours before show) |
| **Template Method** | `printReceipt()` and `printRefundSlip()` share a common border template |

---

*Java Program Explanation | Velalar Cinemas – Scenario 30 | Academic Year 2026-27 (ODD)*
