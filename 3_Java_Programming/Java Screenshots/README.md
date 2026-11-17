# Java Screenshots

**Project:** Scenario 30 – Movie Theatre Ticket Booking and Box Office Analysis System  
**Institution:** Velalar College of Engineering and Technology (Autonomous), Erode – 12  
**Department:** Information Technology | **Academic Year:** 2026-27 (ODD)  
**Class:** 25IT3A & 25IT3B | **Register Numbers:** 30, 60, 90, 120

---

## About These Screenshots

These screenshots capture terminal/console output from the **Velalar Cinemas Java Application** (`com.theatre.app.MovieTheatreApp`) running on JDK 17, Windows 11. They are saved as high-resolution SVG vector images for maximum clarity.

---

## Screenshot Index

| File | Feature Demonstrated |
|------|---------------------|
| [java_screenshot_01_admin_panel.svg](java_screenshot_01_admin_panel.svg) | Admin Panel with all management sub-menus |
| [java_screenshot_02_add_movie.svg](java_screenshot_02_add_movie.svg) | Adding a new movie through the Admin Panel |
| [java_screenshot_03_booking_history.svg](java_screenshot_03_booking_history.svg) | Customer booking history with status tracking |
| [java_screenshot_04_compilation.svg](java_screenshot_04_compilation.svg) | Full compilation output – all 16 `.class` files generated |

> See also **Testing Screenshots/** folder in `1_Software_Engineering/` for additional application run screenshots (booking, cancellation, overbooking error, test suite results).

---

## Run Commands

```powershell
# Compile
javac -d 3_Java_Programming\bin -sourcepath 3_Java_Programming\src 3_Java_Programming\src\com\theatre\app\MovieTheatreApp.java

# Run application
java -cp 3_Java_Programming\bin com.theatre.app.MovieTheatreApp

# Run test suite
java -cp 3_Java_Programming\bin com.theatre.test.BookingServiceTest
```
