# CATEGORY 3: JAVA PROGRAMMING (Course Code: 25ITT32)
## Detailed Requirements Evaluation & Syllabus Alignment Document

### Project Title:
**Movie Theatre Ticket Booking and Box Office Analysis System**
*Assigned Register Numbers:* 30, 60, 90, 120  
**Institution:** Velalar College of Engineering and Technology, Erode - 12  
**Department:** Information Technology | **Class:** 25IT3A & 25IT3B

---

## 1. Executive Summary
The core transactional and business logic engine of the Movie Theatre Management System is engineered in **Java (JDK 27)**. The application adheres to strict Object-Oriented Programming (OOP) paradigms, demonstrates enterprise design patterns, leverages modern Java features (Lambdas, Stream API), implements robust exception handling, and incorporates multithreaded concurrency protection to prevent seat booking race conditions.

---

## 2. Direct Syllabus Mapping (25ITT32: Java Programming)

| Syllabus Unit | Core Theoretical Concepts in Syllabus | Project Implementation / Source Code Reference |
| :--- | :--- | :--- |
| **Unit 1: Introduction to Java** | Java Buzzwords, Data types, Variables, Arrays, Control statements, Classes, Methods, Constructors, Overloading, Static, Console I/O. | - `Movie.java`, `Screen.java`, `MovieTheatreApp.java`<br>- Constructors, overloaded initializers, static ID counters, `Scanner` console I/O for menu navigation. |
| **Unit 2: Inheritance, Packages and Interfaces** | Inheritance basics, `super`, Method Overriding, Dynamic Method Dispatch, Abstract classes, Packages, Member access, Interfaces. | - Package hierarchy: `com.theatre.model`, `com.theatre.service`, `com.theatre.exception`, `com.theatre.concurrency`, `com.theatre.app`.<br>- `Person.java` (abstract superclass) $\to$ `Customer.java` (subclass using `super` & overriding).<br>- Interfaces: `IBookingService.java`, `IReportService.java`. |
| **Unit 3: Exception Handling and Multithreading** | Exception fundamentals, `try-catch-finally`, `throw`, `throws`, User-defined exceptions, Java Thread Model, Synchronization. | - Custom User Exceptions: `SeatUnavailableException`, `InvalidBookingException`, `ShowNotFoundException`.<br>- Multithreaded booking simulator: `ConcurrentBookingSimulator.java`<br>- `synchronized reserveSeats(int count)` in `ShowSchedule.java` preventing double booking. |
| **Unit 4: String Handling and Database Connection** | Strings, String operations, `StringBuilder`, Data conversion, Java Persistence API (JPA) / JDBC database integration. | - `StringBuilder` ticket receipt generation in `Booking.java` and `Cancellation.java`.<br>- Formatted output strings and data conversion (`String.format()`, `Integer.parseInt()`).<br>- Persistence architectural abstraction for JDBC/JPA connectivity. |
| **Unit 5: Generics, Collections, Lambdas & Streams** | Generics, Collections Framework (`List`, `Map`, `Set`), Lambda Expressions, Stream API (filtering, mapping, sorting, reduction, Collectors). | - Collections: `List<Movie>`, `Map<Integer, ShowSchedule>`, `Map<Integer, Booking>`.<br>- Streams & Lambdas in `ReportServiceImpl.java`:<br>&nbsp;&nbsp;• `shows.stream().sorted(Comparator.comparingDouble(...))` (Sorting)<br>&nbsp;&nbsp;• `bookings.stream().collect(Collectors.groupingBy(...))` (Aggregation)<br>&nbsp;&nbsp;• `mapToDouble(Booking::getTotalAmount).sum()` (Reduction). |

---

## 3. Evaluation of Scenario 30 Requirements under Java Programming

### 3.1 Object-Oriented Architecture & Inheritance
- **Base Class:** `Person` encapsulates standard customer demographics (`id`, `name`, `phone`, `email`) and defines the abstract method `getRoleDescription()`.
- **Derived Class:** `Customer` inherits attributes using `super(...)` and overrides `getRoleDescription()` to implement dynamic method dispatch.

### 3.2 Thread Safety & Concurrency Control (Unit 3)
In real-world cinema booking, dozens of users may attempt to reserve tickets for the same movie simultaneously. Without synchronization, two threads can both see 1 seat remaining and book it, causing an illegal overbooking state.
- **Solution:** `ShowSchedule.java` defines `reserveSeats(int count)` with the `synchronized` modifier. Only one thread holds the monitor lock of the show schedule object at any time, guaranteeing mutual exclusion and atomicity:
```java
public synchronized boolean reserveSeats(int count) {
    if (count <= 0) return false;
    if (availableSeats >= count) {
        availableSeats -= count;
        bookedSeats += count;
        return true;
    }
    return false;
}
```

### 3.3 Robust Exception Handling
- Prevent overbooking: Throws `SeatUnavailableException` when requested tickets exceed remaining capacity.
- Prevent invalid input: Throws `InvalidBookingException` on non-positive seat counts or duplicate cancellations.
- Entity lookup failure: Throws `ShowNotFoundException` when referencing non-existent shows.

### 3.4 Functional Programming with Stream API (Unit 5)
`ReportServiceImpl.java` demonstrates functional programming by computing multiplex revenue and occupancy without imperative nested loops:
```java
Map<String, Double> grossRevenueByMovie = bookings.stream()
    .collect(Collectors.groupingBy(
        b -> b.getShow().getMovie().getTitle(),
        Collectors.summingDouble(Booking::getTotalAmount)
    ));
```

---

## 4. Course Outcomes (CO) Addressed
- **CO1:** Apply concepts of classes and objects to model real-world cinema entities.
- **CO2:** Demonstrate inheritance, packages, and interfaces for modular design.
- **CO3:** Implement custom exception handling mechanisms and multithreading synchronization.
- **CO4:** Utilize `StringBuilder` and string handling for formatted receipts and persistence layers.
- **CO5:** Build type-safe applications leveraging generic collections, lambdas, and Stream API aggregations.
