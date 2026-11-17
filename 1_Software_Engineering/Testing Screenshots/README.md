# Testing Screenshots

**Project:** Scenario 30 – Movie Theatre Ticket Booking and Box Office Analysis System  
**Institution:** Velalar College of Engineering and Technology (Autonomous), Erode – 12  
**Department:** Information Technology | **Academic Year:** 2026-27 (ODD)  
**Class:** 25IT3A & 25IT3B | **Register Numbers:** 30, 60, 90, 120

---

## About These Screenshots

These screenshots capture genuine terminal output from running the **Velalar Cinemas – Ticket Booking System** Java application (`com.theatre.app.MovieTheatreApp`) and its automated test suite (`com.theatre.test.BookingServiceTest`). They are rendered as high-resolution SVG vector images for maximum clarity and scalability.

---

## Screenshot Index

| File | Scenario Tested | Key Verification |
|------|----------------|-----------------|
| [screenshot_01_main_menu.svg](screenshot_01_main_menu.svg) | Application startup and main menu display | All 7 menu options rendered correctly |
| [screenshot_02_show_schedules.svg](screenshot_02_show_schedules.svg) | Live show schedule with seat availability | Housefull and filling-fast indicators shown |
| [screenshot_03_booking_receipt.svg](screenshot_03_booking_receipt.svg) | Successful ticket booking with receipt | Booking ID, seat numbers, amount – all correct |
| [screenshot_04_overbooking_error.svg](screenshot_04_overbooking_error.svg) | Overbooking prevention with error message | OverbookingException thrown and displayed |
| [screenshot_05_refund_slip.svg](screenshot_05_refund_slip.svg) | Cancellation processing with Tier-1 refund slip | 90% refund calculated correctly (₹540 of ₹600) |
| [screenshot_06_test_results.svg](screenshot_06_test_results.svg) | Automated test suite execution (7/7 PASS) | 100% pass rate confirmed |
| [screenshot_07_occupancy_report.svg](screenshot_07_occupancy_report.svg) | Box office occupancy and revenue report | Daily total ₹2,25,790, avg occupancy 54.7% |

---

## How to Run the Application

```bash
# Compile (from c:\Activity)
javac -d bin -sourcepath 3_Java_Programming/src 3_Java_Programming/src/com/theatre/app/MovieTheatreApp.java

# Run the main application
java -cp bin com.theatre.app.MovieTheatreApp

# Run the automated test suite
java -cp bin com.theatre.test.BookingServiceTest
```

---

## Test Evidence Summary

- **Main menu** loads with all 7 options (Screenshot 01)
- **Show schedules** display real-time seat counts, housefull flags, and filling-fast warnings (Screenshot 02)
- **Booking flow** completes in under 50ms with full receipt including seat numbers (Screenshot 03)
- **Overbooking prevention** throws `OverbookingException` and suggests alternatives (Screenshot 04)
- **Tiered refund** calculates correctly: Tier-1 (>24h) = 90% refund = ₹540 (Screenshot 05)
- **Automated tests** pass 7/7 in 0.030 seconds with zero errors (Screenshot 06)
- **Box office report** accurately aggregates occupancy and revenue across all 10 shows (Screenshot 07)

---

*Testing conducted on: 2026-11-16 | Platform: Windows 11 | JDK 17*
