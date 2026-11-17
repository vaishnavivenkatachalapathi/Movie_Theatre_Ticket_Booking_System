import java.io.*;
import java.nio.file.*;
import java.sql.*;
import java.util.*;

public class InteractiveDBSimulator {
    private static class Movie {
        String id, title, genre, language;
        int duration;
        double rating;
        Movie(String id, String title, String genre, String lang, int d, double r) {
            this.id=id; this.title=title; this.genre=genre; this.language=lang; this.duration=d; this.rating=r;
        }
    }

    private static class Screen {
        int id; String name; int capacity;
        Screen(int id, String name, int cap) { this.id=id; this.name=name; this.capacity=cap; }
    }

    private static class Show {
        int id; String movieId; int screenId; String time; double price; int availableSeats;
        Show(int id, String mid, int sid, String time, double price, int avail) {
            this.id=id; this.movieId=mid; this.screenId=sid; this.time=time; this.price=price; this.availableSeats=avail;
        }
    }

    private static class Booking {
        int id; String customer; int showId; int tickets; double total; String status;
        Booking(int id, String c, int sid, int t, double tot, String st) {
            this.id=id; this.customer=c; this.showId=sid; this.tickets=t; this.total=tot; this.status=st;
        }
    }

    private static Map<String, Movie> movies = new LinkedHashMap<>();
    private static Map<Integer, Screen> screens = new LinkedHashMap<>();
    private static Map<Integer, Show> shows = new LinkedHashMap<>();
    private static List<Booking> bookings = new ArrayList<>();

    public static void main(String[] args) {
        initSampleData();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n===============================================================================");
            System.out.println("   VELALAR COLLEGE OF ENGINEERING AND TECHNOLOGY (AUTONOMOUS), ERODE - 12");
            System.out.println("          DEPARTMENT OF INFORMATION TECHNOLOGY - ACADEMIC YEAR 2026-27");
            System.out.println("     DATABASE MANAGEMENT SYSTEMS (25ITT31) - INTERACTIVE SQL & QUERY ENGINE");
            System.out.println("===============================================================================");
            System.out.println(" 1. View All Movies (SELECT * FROM movies)");
            System.out.println(" 2. View All Screens & Capacity (SELECT * FROM screens)");
            System.out.println(" 3. View Show Schedules with Live Available Seats (vw_show_schedule_occupancy)");
            System.out.println(" 4. Execute Movie Revenue & Ranking Query (Aggregates with GROUP BY & ORDER BY)");
            System.out.println(" 5. Execute Screen Occupancy Rate Analysis Query");
            System.out.println(" 6. Add a New Movie Record Manually (INSERT INTO movies VALUES (...))");
            System.out.println(" 7. Perform a Ticket Booking Transaction with Trigger Simulation (Atomicity)");
            System.out.println(" 8. Cancel a Booking with Refund & Trigger Auto-Seat-Restoration");
            System.out.println(" 9. Export All Query Outputs to Text File (program_execution_output.txt)");
            System.out.println(" 0. Exit Database Engine");
            System.out.println("-------------------------------------------------------------------------------");
            System.out.print("Enter your option (0-9): ");

            String choice = sc.nextLine().trim();
            if (choice.equals("0")) {
                System.out.println("Exiting Database Engine. Goodbye!");
                break;
            }

            switch (choice) {
                case "1": selectMovies(); break;
                case "2": selectScreens(); break;
                case "3": selectShows(); break;
                case "4": queryMovieRevenue(); break;
                case "5": queryScreenOccupancy(); break;
                case "6": insertMovieManual(sc); break;
                case "7": insertBookingTransaction(sc); break;
                case "8": cancelBookingTransaction(sc); break;
                case "9": exportOutputsToFile(); break;
                default: System.out.println("Invalid option! Enter 0-9.");
            }
        }
    }

    private static void initSampleData() {
        movies.put("M001", new Movie("M001", "Avatar: The Way of Water", "Sci-Fi", "English", 192, 8.8));
        movies.put("M002", new Movie("M002", "Jailer", "Action", "Tamil", 168, 8.5));
        movies.put("M003", new Movie("M003", "Leo", "Action", "Tamil", 164, 8.2));
        movies.put("M004", new Movie("M004", "Ponniyin Selvan Part 2", "Historical", "Tamil", 164, 8.4));
        movies.put("M005", new Movie("M005", "Interstellar: Re-release", "Sci-Fi", "English", 169, 8.9));
        movies.put("M006", new Movie("M006", "Oppenheimer", "Biography", "English", 180, 8.9));

        screens.put(1, new Screen(1, "Audi 1 - IMAX Laser", 120));
        screens.put(2, new Screen(2, "Audi 2 - Dolby Atmos 4K", 100));
        screens.put(3, new Screen(3, "Audi 3 - RealD 3D Premium", 80));
        screens.put(4, new Screen(4, "Audi 4 - VIP Recliner Lounge", 50));

        shows.put(501, new Show(501, "M001", 1, "10:00 AM", 250.00, 30));
        shows.put(502, new Show(502, "M001", 1, "02:30 PM", 250.00, 18));
        shows.put(503, new Show(503, "M001", 1, "07:00 PM", 300.00, 6));
        shows.put(504, new Show(504, "M002", 2, "11:00 AM", 180.00, 18));
        shows.put(505, new Show(505, "M002", 2, "03:30 PM", 180.00, 22));
        shows.put(506, new Show(506, "M002", 2, "08:00 PM", 220.00, 8));
        shows.put(507, new Show(507, "M003", 3, "06:30 PM", 200.00, 10));

        bookings.add(new Booking(1001, "Vaishnavi V", 501, 4, 1000.00, "CONFIRMED"));
        bookings.add(new Booking(1002, "Karthik S", 501, 2, 500.00, "CONFIRMED"));
        bookings.add(new Booking(1003, "Dharani R", 502, 5, 1250.00, "CONFIRMED"));
        bookings.add(new Booking(1004, "Sanjay Kumar", 503, 6, 1800.00, "CONFIRMED"));
        bookings.add(new Booking(1005, "Anand R", 504, 4, 720.00, "CANCELLED"));
        bookings.add(new Booking(1006, "Deepa M", 505, 2, 360.00, "CONFIRMED"));
        bookings.add(new Booking(1007, "Vignesh P", 506, 8, 1760.00, "CONFIRMED"));
    }

    private static void selectMovies() {
        System.out.println("\n--- Query Output: SELECT * FROM movies ORDER BY movie_id; ---");
        System.out.printf("%-8s %-28s %-12s %-10s %-10s %-6s\n", "movie_id", "title", "genre", "language", "duration", "rating");
        System.out.println("-------------------------------------------------------------------------------");
        for (Movie m : movies.values()) {
            System.out.printf("%-8s %-28s %-12s %-10s %-10d %-6.1f\n", m.id, m.title, m.genre, m.language, m.duration, m.rating);
        }
        System.out.println("Rows returned: " + movies.size());
    }

    private static void selectScreens() {
        System.out.println("\n--- Query Output: SELECT * FROM screens; ---");
        System.out.printf("%-10s %-32s %-15s\n", "screen_id", "screen_name", "total_capacity");
        System.out.println("-------------------------------------------------------------------------------");
        for (Screen s : screens.values()) {
            System.out.printf("%-10d %-32s %-15d\n", s.id, s.name, s.capacity);
        }
        System.out.println("Rows returned: " + screens.size());
    }

    private static void selectShows() {
        System.out.println("\n--- Query Output: SELECT * FROM vw_show_schedule_occupancy; ---");
        System.out.printf("%-8s %-25s %-25s %-10s %-10s %-12s\n", "show_id", "movie_title", "screen_name", "show_time", "price", "avail_seats");
        System.out.println("-------------------------------------------------------------------------------");
        for (Show s : shows.values()) {
            Movie m = movies.get(s.movieId);
            Screen sc = screens.get(s.screenId);
            System.out.printf("%-8d %-25s %-25s %-10s Rs. %-6.2f %-12d\n",
                    s.id, (m != null ? m.title : "N/A"), (sc != null ? sc.name : "N/A"), s.time, s.price, s.availableSeats);
        }
        System.out.println("Rows returned: " + shows.size());
    }

    private static void queryMovieRevenue() {
        System.out.println("\n--- Query Output: SELECT m.title, SUM(b.total_amount) AS revenue, COUNT(b.booking_id) AS orders");
        System.out.println("                  FROM movies m JOIN shows s ON m.movie_id=s.movie_id");
        System.out.println("                  JOIN bookings b ON s.show_id=b.show_id WHERE b.status='CONFIRMED'");
        System.out.println("                  GROUP BY m.title ORDER BY revenue DESC; ---");
        System.out.printf("%-28s %-15s %-15s\n", "movie_title", "total_orders", "gross_revenue");
        System.out.println("-------------------------------------------------------------------------------");

        Map<String, Double> revMap = new HashMap<>();
        Map<String, Integer> orderMap = new HashMap<>();

        for (Booking b : bookings) {
            if (!b.status.equals("CONFIRMED")) continue;
            Show s = shows.get(b.showId);
            if (s != null) {
                Movie m = movies.get(s.movieId);
                if (m != null) {
                    revMap.put(m.title, revMap.getOrDefault(m.title, 0.0) + b.total);
                    orderMap.put(m.title, orderMap.getOrDefault(m.title, 0) + 1);
                }
            }
        }

        List<Map.Entry<String, Double>> list = new ArrayList<>(revMap.entrySet());
        list.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        double grandTotal = 0;
        for (Map.Entry<String, Double> e : list) {
            System.out.printf("%-28s %-15d Rs. %-12.2f\n", e.getKey(), orderMap.get(e.getKey()), e.getValue());
            grandTotal += e.getValue();
        }
        System.out.println("-------------------------------------------------------------------------------");
        System.out.printf("Combined Confirmed Revenue: Rs. %.2f\n", grandTotal);
    }

    private static void queryScreenOccupancy() {
        System.out.println("\n--- Query Output: Screen-wise Capacity vs Utilization ---");
        System.out.printf("%-10s %-28s %-10s %-10s %-12s\n", "screen_id", "screen_name", "capacity", "sold_seats", "occupancy");
        System.out.println("-------------------------------------------------------------------------------");
        for (Screen sc : screens.values()) {
            int sold = 0;
            int showCount = 0;
            for (Show s : shows.values()) {
                if (s.screenId == sc.id) {
                    sold += (sc.capacity - s.availableSeats);
                    showCount++;
                }
            }
            int totalPossible = showCount * sc.capacity;
            double pct = totalPossible > 0 ? (sold * 100.0) / totalPossible : 0.0;
            System.out.printf("%-10d %-28s %-10d %-10d %6.1f%%\n", sc.id, sc.name, sc.capacity, sold, pct);
        }
    }

    private static void insertMovieManual(Scanner sc) {
        System.out.println("\n--- Manual Input: INSERT INTO movies VALUES (...) ---");
        try {
            System.out.print("Enter Movie ID (e.g. M007): ");
            String id = sc.nextLine().trim();
            System.out.print("Enter Movie Title: ");
            String title = sc.nextLine().trim();
            System.out.print("Enter Genre: ");
            String genre = sc.nextLine().trim();
            System.out.print("Enter Language: ");
            String lang = sc.nextLine().trim();
            System.out.print("Enter Duration in minutes: ");
            int dur = Integer.parseInt(sc.nextLine().trim());
            System.out.print("Enter Rating (e.g. 8.5): ");
            double rate = Double.parseDouble(sc.nextLine().trim());

            movies.put(id, new Movie(id, title, genre, lang, dur, rate));
            System.out.println("✓ Query executed successfully: 1 row inserted into 'movies' (PK: " + id + ").");
        } catch (Exception e) {
            System.out.println("SQL Execution Error: Invalid data format!");
        }
    }

    private static void insertBookingTransaction(Scanner sc) {
        System.out.println("\n--- Manual Input: START TRANSACTION; INSERT INTO bookings ...; COMMIT; ---");
        try {
            System.out.print("Enter Customer Name: ");
            String c = sc.nextLine().trim();
            System.out.print("Enter Show ID (Available: 501, 502, 503, 504, 505, 506, 507): ");
            int sid = Integer.parseInt(sc.nextLine().trim());
            Show s = shows.get(sid);
            if (s == null) {
                System.out.println("ERROR: Foreign Key Constraint Violation! Show ID " + sid + " does not exist.");
                return;
            }
            System.out.printf("Show Details: Movie=%s | Available Seats=%d | Price=Rs. %.2f\n",
                    movies.get(s.movieId).title, s.availableSeats, s.price);

            System.out.print("Enter Number of Tickets to Book: ");
            int t = Integer.parseInt(sc.nextLine().trim());

            if (t <= 0) {
                System.out.println("ERROR: CHECK Constraint (tickets > 0) violated!");
                return;
            }

            if (t > s.availableSeats) {
                System.out.println("ROLLBACK TRANSACTION! Insufficient available seats (Requested: " + t + ", Available: " + s.availableSeats + ").");
                return;
            }

            // Trigger simulation
            s.availableSeats -= t;
            int newBkId = 1000 + bookings.size() + 1;
            double tot = t * s.price;
            bookings.add(new Booking(newBkId, c, sid, t, tot, "CONFIRMED"));

            System.out.println("COMMIT TRANSACTION;");
            System.out.println("✓ Booking #" + newBkId + " recorded successfully!");
            System.out.println("✓ Trigger 'trg_update_seats_on_booking' fired: Show #" + sid + " available seats updated to " + s.availableSeats + ".");
        } catch (Exception e) {
            System.out.println("Transaction Aborted due to error: " + e.getMessage());
        }
    }

    private static void cancelBookingTransaction(Scanner sc) {
        System.out.println("\n--- Manual Input: UPDATE bookings SET status='CANCELLED' ... ---");
        try {
            System.out.print("Enter Booking ID to Cancel (e.g. 1001, 1002, 1003): ");
            int bid = Integer.parseInt(sc.nextLine().trim());
            Booking target = null;
            for (Booking b : bookings) {
                if (b.id == bid) { target = b; break; }
            }

            if (target == null) {
                System.out.println("ERROR: Booking ID " + bid + " not found!");
                return;
            }

            if (target.status.equals("CANCELLED")) {
                System.out.println("ERROR: Booking #" + bid + " is ALREADY cancelled!");
                return;
            }

            target.status = "CANCELLED";
            Show s = shows.get(target.showId);
            if (s != null) {
                s.availableSeats += target.tickets;
            }

            double refund = target.total * 0.90; // Tier 1: 90%
            System.out.println("✓ Booking #" + bid + " updated to CANCELLED.");
            System.out.printf("✓ Refund of 90%% disbursed: Rs. %.2f\n", refund);
            System.out.println("✓ Trigger 'trg_restore_seats_on_cancellation' fired: Show #" + target.showId + " restored " + target.tickets + " seats (Now: " + (s != null ? s.availableSeats : 0) + ").");
        } catch (Exception e) {
            System.out.println("Cancellation failed: " + e.getMessage());
        }
    }

    private static void exportOutputsToFile() {
        StringBuilder sb = new StringBuilder();
        sb.append("===============================================================================\n");
        sb.append("   VELALAR COLLEGE OF ENGINEERING AND TECHNOLOGY (AUTONOMOUS), ERODE - 12\n");
        sb.append("          DEPARTMENT OF INFORMATION TECHNOLOGY - ACADEMIC YEAR 2026-27\n");
        sb.append("   DATABASE MANAGEMENT SYSTEMS (25ITT31) - SQL QUERY EXECUTION OUTPUT\n");
        sb.append("===============================================================================\n\n");

        sb.append("1. QUERY: SELECT * FROM movies;\n");
        sb.append("-------------------------------------------------------------------------------\n");
        for (Movie m : movies.values()) {
            sb.append(String.format("%-8s %-28s %-12s %-10s %d min\n", m.id, m.title, m.genre, m.language, m.duration));
        }

        sb.append("\n2. QUERY: SELECT * FROM screens;\n");
        sb.append("-------------------------------------------------------------------------------\n");
        for (Screen sc : screens.values()) {
            sb.append(String.format("Screen #%d: %-30s | Capacity: %d seats\n", sc.id, sc.name, sc.capacity));
        }

        sb.append("\n3. QUERY: SELECT * FROM vw_show_schedule_occupancy;\n");
        sb.append("-------------------------------------------------------------------------------\n");
        for (Show s : shows.values()) {
            sb.append(String.format("Show #%d | Movie: %-25s | Screen #%d | Time: %-10s | Avail: %d seats\n",
                    s.id, movies.get(s.movieId).title, s.screenId, s.time, s.availableSeats));
        }

        sb.append("\n4. QUERY: MOVIE REVENUE & BOOKINGS SUMMARY\n");
        sb.append("-------------------------------------------------------------------------------\n");
        for (Booking b : bookings) {
            sb.append(String.format("Booking #%d | Customer: %-15s | Show #%d | Tickets: %d | Amount: Rs. %.2f | Status: %s\n",
                    b.id, b.customer, b.showId, b.tickets, b.total, b.status));
        }

        try {
            Files.writeString(Paths.get("program_execution_output.txt"), sb.toString());
            System.out.println("✓ Successfully generated text report: program_execution_output.txt");
        } catch (Exception e) {
            System.out.println("Error writing report: " + e.getMessage());
        }
    }
}
