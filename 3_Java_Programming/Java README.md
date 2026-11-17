# Java README

**Project:** Scenario 30 – Movie Theatre Ticket Booking and Box Office Analysis System  
**Institution:** Velalar College of Engineering and Technology (Autonomous), Erode – 12  
**Department:** Information Technology | **Academic Year:** 2026-27 (ODD)  
**Class:** 25IT3A & 25IT3B | **Register Numbers:** 30, 60, 90, 120

---

## Overview

This Java module implements the full console-based **Movie Theatre Ticket Booking System** using object-oriented programming principles. The application handles movie scheduling, seat booking, concurrent seat allocation (thread-safe), tiered cancellation/refunds, and box office reporting.

---

## Prerequisites

| Requirement | Minimum Version | Check Command |
|-------------|----------------|---------------|
| Java Development Kit (JDK) | JDK 11+ (JDK 17 recommended) | `java -version` |
| Operating System | Windows 10/11, Linux, or macOS | — |
| MySQL Server *(optional)* | 8.0+ | `mysql --version` |
| MySQL Connector/J *(optional)* | 8.0+ | See JDBC Setup section |

> [!NOTE] The application runs in **in-memory simulation mode** by default and does NOT require MySQL to demonstrate all features. MySQL connectivity is an optional extension (see `dao/` package).

---

## Project Structure

```
3_Java_Programming/
├── src/
│   └── com/theatre/
│       ├── model/              # Domain model classes
│       │   ├── Movie.java
│       │   ├── Screen.java
│       │   ├── ShowSchedule.java
│       │   ├── Customer.java
│       │   ├── Booking.java
│       │   └── Cancellation.java
│       ├── exception/          # Custom exceptions
│       │   ├── OverbookingException.java
│       │   ├── InvalidBookingException.java
│       │   └── ShowNotFoundException.java
│       ├── service/            # Business logic layer
│       │   ├── BookingService.java
│       │   ├── CancellationService.java
│       │   └── ReportService.java
│       ├── concurrency/        # Thread-safe seat allocator
│       │   └── ConcurrentBookingSimulator.java
│       ├── dao/                # Data Access Objects (MySQL JDBC)
│       │   └── BookingDAO.java
│       ├── test/               # Automated test suite
│       │   └── BookingServiceTest.java
│       └── app/                # Main application entry point
│           └── MovieTheatreApp.java
├── bin/                        # Compiled .class files (auto-generated)
├── Java README.md
├── Java_Syllabus_Evaluation.md
├── Java Program Explanation.md
├── Java Testing Results.md
└── Java Screenshots/
```

---

## Step 1 – Compile the Application

Open **Command Prompt** or **PowerShell** and navigate to the project root:

```powershell
cd c:\Activity
```

### Option A – Compile All Files at Once (Recommended)

```powershell
javac -d 3_Java_Programming\bin -sourcepath 3_Java_Programming\src 3_Java_Programming\src\com\theatre\app\MovieTheatreApp.java 3_Java_Programming\src\com\theatre\test\BookingServiceTest.java
```

### Option B – Compile Package by Package

```powershell
# 1. Compile model classes
javac -d 3_Java_Programming\bin 3_Java_Programming\src\com\theatre\model\*.java

# 2. Compile exception classes
javac -d 3_Java_Programming\bin 3_Java_Programming\src\com\theatre\exception\*.java

# 3. Compile service classes (depends on model + exception)
javac -cp 3_Java_Programming\bin -d 3_Java_Programming\bin 3_Java_Programming\src\com\theatre\service\*.java

# 4. Compile concurrency module
javac -cp 3_Java_Programming\bin -d 3_Java_Programming\bin 3_Java_Programming\src\com\theatre\concurrency\*.java

# 5. Compile DAO layer
javac -cp 3_Java_Programming\bin -d 3_Java_Programming\bin 3_Java_Programming\src\com\theatre\dao\*.java

# 6. Compile test suite
javac -cp 3_Java_Programming\bin -d 3_Java_Programming\bin 3_Java_Programming\src\com\theatre\test\*.java

# 7. Compile main application
javac -cp 3_Java_Programming\bin -d 3_Java_Programming\bin 3_Java_Programming\src\com\theatre\app\*.java
```

**Expected output:** No errors. `bin\com\theatre\` directories will contain `.class` files.

---

## Step 2 – Run the Main Application

```powershell
java -cp 3_Java_Programming\bin com.theatre.app.MovieTheatreApp
```

Or use the batch file shortcut from the project root:

```powershell
run_java_app.bat
```

**You will see:**
```
================================================
   VELALAR CINEMAS – TICKET BOOKING SYSTEM
================================================
  Scenario 30 | Dept. of IT | Velalar College
  Academic Year: 2026-27 (ODD)
================================================
                  MAIN MENU
================================================
  1.  View All Shows & Seat Availability
  2.  Book Ticket(s)
  3.  Cancel Booking
  4.  View My Bookings
  5.  Box Office / Occupancy Report
  6.  Admin Panel
  7.  Run Automated Test Suite
  0.  Exit
================================================
Enter your choice:
```

---

## Step 3 – Run the Automated Test Suite

```powershell
java -cp 3_Java_Programming\bin com.theatre.test.BookingServiceTest
```

Or use the batch file:

```powershell
run_java_tests.bat
```

**Expected output:**
```
✓  PASS  TC-01  testSuccessfulBooking          (0.004s)
✓  PASS  TC-02  testOverbookingPrevention       (0.002s)
✓  PASS  TC-03  testFullRefund_24HoursPlus      (0.003s)
✓  PASS  TC-04  testPartialRefund_12To24Hours   (0.002s)
✓  PASS  TC-05  testNoRefund_Under2Hours        (0.003s)
✓  PASS  TC-06  testConcurrentBooking_Safety    (0.011s)
✓  PASS  TC-07  testBoxOfficeReport_Accuracy    (0.005s)
─────────────────────────────────────────────
Tests: 7  |  Passed: 7  |  Failed: 0  |  100%
BUILD SUCCESSFUL
```

---

## Step 4 – Run the Concurrency Simulation

```powershell
java -cp 3_Java_Programming\bin com.theatre.concurrency.ConcurrentBookingSimulator
```

This launches 10 concurrent booking threads targeting the same show, demonstrating the `ReentrantLock`-based thread-safe seat allocation.

---

## JDBC Setup (Optional – MySQL Integration)

To connect the application to a live MySQL database:

### 1. Download MySQL Connector/J

Download from: https://dev.mysql.com/downloads/connector/j/

Or if using Maven, add to `pom.xml`:
```xml
<dependency>
  <groupId>com.mysql</groupId>
  <artifactId>mysql-connector-j</artifactId>
  <version>8.3.0</version>
</dependency>
```

### 2. Add Connector to Classpath

```powershell
# Replace <version> with your downloaded version
java -cp "3_Java_Programming\bin;lib\mysql-connector-j-8.3.0.jar" com.theatre.app.MovieTheatreApp
```

### 3. Configure Database Connection

In `BookingDAO.java`, update the connection constants:
```java
private static final String URL  = "jdbc:mysql://localhost:3306/velalar_cinemas";
private static final String USER = "root";
private static final String PASS = "your_password";
```

### 4. Initialise Database

```sql
-- Run in MySQL client
SOURCE c:/Activity/2_Database_Management_Systems/schema.sql;
SOURCE c:/Activity/2_Database_Management_Systems/sample_data.sql;
```

---

## Troubleshooting

| Error | Cause | Fix |
|-------|-------|-----|
| `javac: command not found` | JDK not installed or not on PATH | Install JDK 17; add `%JAVA_HOME%\bin` to PATH |
| `ClassNotFoundException` | Incorrect classpath | Ensure `-cp` points to `bin\` not `src\` |
| `Error: Could not find or load main class` | Wrong class name | Use `com.theatre.app.MovieTheatreApp` (fully qualified) |
| `? ` shown instead of `₹` | Console encoding issue | Run `chcp 65001` before the java command to set UTF-8 |
| `Communications link failure` | MySQL not running | Start MySQL service or use in-memory mode |

---

## Quick Reference – Key Classes

| Class | Package | Role |
|-------|---------|------|
| `MovieTheatreApp` | `app` | Main entry point – menu loop |
| `BookingService` | `service` | Booking logic, seat validation |
| `CancellationService` | `service` | Tiered refund calculation |
| `ReportService` | `service` | Occupancy and revenue reports |
| `ConcurrentBookingSimulator` | `concurrency` | Thread-safe multi-user booking demo |
| `BookingServiceTest` | `test` | 7-test automated test suite |
| `OverbookingException` | `exception` | Thrown when seats insufficient |

---

*Java README | Velalar Cinemas – Scenario 30 | Academic Year 2026-27 (ODD)*
