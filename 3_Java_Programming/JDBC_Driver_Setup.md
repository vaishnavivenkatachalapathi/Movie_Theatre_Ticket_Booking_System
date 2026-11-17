# JDBC Driver Setup and Database Configuration

**Project:** Scenario 30 – Movie Theatre Ticket Booking and Box Office Analysis System  
**Institution:** Velalar College of Engineering and Technology (Autonomous), Erode – 12  
**Department:** Information Technology | **Academic Year:** 2026-27 (ODD)  
**Class:** 25IT3A & 25IT3B | **Register Numbers:** 30, 60, 90, 120

---

## 1. Overview

The **Velalar Cinemas** Java application is built as a pure, lightweight Java Standard Edition (Java SE) application. It is designed to run seamlessly in **two operating modes**:

1. **In-Memory Simulation Mode (Default & Zero-Dependency):**
   - Runs out-of-the-box with standard Java (JDK 17+).
   - Does **not** require external JAR downloads, internet connection, or Maven repository access.
   - Provides full booking workflows, synchronized thread-safety, tiered cancellation, and reporting.

2. **MySQL Database Mode (Optional Database Persistence):**
   - Connects to the local `velalar_cinemas` MySQL database schema via standard JDBC (`com.theatre.dao.DBConnection` and `BookingDAO`).
   - Uses the official **MySQL Connector/J** driver JAR.

---

## 2. JDBC Driver Information

| Attribute | Specification |
| :--- | :--- |
| **Driver Name** | MySQL Connector/J |
| **Recommended Version** | `8.3.0` (or `8.0.x`) |
| **Driver Class Name** | `com.mysql.cj.jdbc.Driver` |
| **Connection URL** | `jdbc:mysql://localhost:3306/velalar_cinemas?useSSL=false&allowPublicKeyRetrieval=true` |
| **Default Username** | `root` |
| **Default Password** | `root` (configurable in `DBConnection.java`) |

---

## 3. How to Obtain and Configure MySQL Connector/J

### Method 1: Local Offline JAR (Recommended for College Lab Environments)
1. Download or copy `mysql-connector-j-8.3.0.jar` (or `mysql-connector-java-8.0.x.jar`).
2. Place the JAR file into a `lib/` directory inside `c:\Activity\3_Java_Programming\lib\`.
3. Compile and execute with the `-cp` (classpath) argument:

```powershell
# Compile including driver on classpath
javac -d 3_Java_Programming\bin -cp "3_Java_Programming\lib\*" 3_Java_Programming\src\com\theatre\app\MovieTheatreApp.java

# Run application with MySQL driver attached
java -cp "3_Java_Programming\bin;3_Java_Programming\lib\*" com.theatre.app.MovieTheatreApp
```

### Method 2: Maven Configuration (Only when active Internet is available)
If your environment has unrestricted internet access and Maven installed, you can use the following standard dependency block:

```xml
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <version>8.3.0</version>
</dependency>
```

> [!NOTE]
> Offline IDE environments or firewalled college labs will report `UnknownHostException: repo.maven.apache.org` if Maven tries to fetch dependencies without internet access. In such cases, use the standard pure JDK mode or place the JAR in `lib/` directly.

---

## 4. Verification in Code (`DBConnection.java`)

The database connection class handles driver loading gracefully:

```java
package com.theatre.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/velalar_cinemas";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found on classpath.");
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
```

---

## 5. Instant Execution Commands (Pure Java)

No Maven or internet required:

```powershell
# Run the complete application
c:\Activity\run_java_app.bat

# Run the 7/7 automated test suite
c:\Activity\run_java_tests.bat
```
