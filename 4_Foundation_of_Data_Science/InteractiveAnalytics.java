import java.io.*;
import java.nio.file.*;
import java.util.*;

public class InteractiveAnalytics {
    private static List<Map<String, String>> records = new ArrayList<>();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        loadData("theatre_bookings.csv");

        while (true) {
            System.out.println("\n===============================================================================");
            System.out.println("   VELALAR COLLEGE OF ENGINEERING AND TECHNOLOGY (AUTONOMOUS), ERODE - 12");
            System.out.println("          DEPARTMENT OF INFORMATION TECHNOLOGY - ACADEMIC YEAR 2026-27");
            System.out.println("        FOUNDATIONS OF DATA SCIENCE (25ITC31) - INTERACTIVE ANALYTICS");
            System.out.println("===============================================================================");
            System.out.println(" 1. View Summary Statistics of Dataset (Central Tendency & Variance)");
            System.out.println(" 2. Compare Movie Popularity by Number of Tickets Sold (Manual Query)");
            System.out.println(" 3. View Day-by-Day Daily Booking Trends");
            System.out.println(" 4. Filter Bookings by Genre or Language (Manual Filter)");
            System.out.println(" 5. Perform Statistical Analysis on a Custom Ticket Price (Z-Score & IQR)");
            System.out.println(" 6. Add a New Custom Booking Record Manually");
            System.out.println(" 7. Export Current Analytics Output to Text File (program_execution_output.txt)");
            System.out.println(" 0. Exit Analytics Engine");
            System.out.println("-------------------------------------------------------------------------------");
            System.out.print("Enter your option (0-7): ");

            String choice = sc.nextLine().trim();

            if (choice.equals("0")) {
                System.out.println("Exiting Data Science Analytics Engine. Goodbye!");
                break;
            }

            switch (choice) {
                case "1":
                    displaySummaryStats();
                    break;
                case "2":
                    displayMoviePopularity(sc);
                    break;
                case "3":
                    displayDailyBookingTrends();
                    break;
                case "4":
                    filterBookings(sc);
                    break;
                case "5":
                    calculatePriceZScore(sc);
                    break;
                case "6":
                    addNewBooking(sc);
                    break;
                case "7":
                    exportOutputToFile();
                    break;
                default:
                    System.out.println("Invalid option! Please enter a number between 0 and 7.");
            }
        }
    }

    private static void loadData(String filename) {
        records.clear();
        File file = new File(filename);
        if (!file.exists()) {
            file = new File("4_Foundation_of_Data_Science/" + filename);
        }
        if (!file.exists()) {
            System.out.println("Dataset file not found: " + filename);
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String headerLine = br.readLine();
            if (headerLine == null) return;
            String[] headers = headerLine.split(",");

            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] values = line.split(",");
                Map<String, String> row = new HashMap<>();
                for (int i = 0; i < headers.length && i < values.length; i++) {
                    row.put(headers[i].trim(), values[i].trim());
                }
                records.add(row);
            }
            System.out.println(">>> Ingested " + records.size() + " booking records from " + file.getName() + " successfully.");
        } catch (Exception e) {
            System.out.println("Error reading dataset: " + e.getMessage());
        }
    }

    private static void displaySummaryStats() {
        System.out.println("\n-------------------------------------------------------------------------------");
        System.out.println("             DESCRIPTIVE SUMMARY STATISTICS (NUMERICAL METRICS)");
        System.out.println("-------------------------------------------------------------------------------");
        List<Double> tickets = new ArrayList<>();
        List<Double> grossAmounts = new ArrayList<>();
        List<Double> occupancies = new ArrayList<>();

        for (Map<String, String> r : records) {
            try {
                tickets.add(Double.parseDouble(r.get("number_of_tickets")));
                grossAmounts.add(Double.parseDouble(r.get("gross_amount")));
                occupancies.add(Double.parseDouble(r.get("show_occupancy_pct")));
            } catch (Exception ignored) {}
        }

        printMetricStats("Number of Tickets", tickets);
        printMetricStats("Gross Revenue (INR)", grossAmounts);
        printMetricStats("Show Occupancy Rate (%)", occupancies);
    }

    private static void printMetricStats(String label, List<Double> data) {
        if (data.isEmpty()) return;
        Collections.sort(data);
        double sum = 0;
        for (double d : data) sum += d;
        double mean = sum / data.size();

        double median;
        int n = data.size();
        if (n % 2 == 0) median = (data.get(n/2 - 1) + data.get(n/2)) / 2.0;
        else median = data.get(n/2);

        double varSum = 0;
        for (double d : data) varSum += Math.pow(d - mean, 2);
        double stdDev = Math.sqrt(varSum / n);

        double min = data.get(0);
        double max = data.get(n - 1);
        double q1 = data.get((int)(n * 0.25));
        double q3 = data.get((int)(n * 0.75));
        double iqr = q3 - q1;

        System.out.printf("[+] %s (N=%d):\n", label, n);
        System.out.printf("    Mean: %.2f | Median: %.2f | Min: %.2f | Max: %.2f\n", mean, median, min, max);
        System.out.printf("    Standard Deviation: %.2f | Q1: %.2f | Q3: %.2f | IQR: %.2f\n\n", stdDev, q1, q3, iqr);
    }

    private static void displayMoviePopularity(Scanner sc) {
        System.out.println("\n-------------------------------------------------------------------------------");
        System.out.println("      MOVIE POPULARITY RANKING (BY NUMBER OF TICKETS SOLD, NOT REVENUE)");
        System.out.println("-------------------------------------------------------------------------------");
        Map<String, Integer> ticketCounts = new HashMap<>();
        Map<String, Integer> bookingCounts = new HashMap<>();

        for (Map<String, String> r : records) {
            String m = r.get("movie_title");
            int t = Integer.parseInt(r.getOrDefault("number_of_tickets", "0"));
            ticketCounts.put(m, ticketCounts.getOrDefault(m, 0) + t);
            bookingCounts.put(m, bookingCounts.getOrDefault(m, 0) + 1);
        }

        List<Map.Entry<String, Integer>> list = new ArrayList<>(ticketCounts.entrySet());
        list.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        int totalTickets = 0;
        for (int t : ticketCounts.values()) totalTickets += t;

        System.out.printf("%-4s %-28s %-15s %-15s %-12s\n", "Rank", "Movie Title", "Tickets Sold", "Bookings", "Market Share");
        System.out.println("-------------------------------------------------------------------------------");
        int rank = 1;
        for (Map.Entry<String, Integer> e : list) {
            double share = (e.getValue() * 100.0) / totalTickets;
            System.out.printf("%-4d %-28s %-15d %-15d %6.1f%%\n", rank++, e.getKey(), e.getValue(), bookingCounts.get(e.getKey()), share);
        }
        System.out.println("-------------------------------------------------------------------------------");
        System.out.printf("Total Tickets Sold Across Catalog: %d tickets in %d transactions\n", totalTickets, records.size());

        System.out.print("\nWould you like to search ticket details for a specific movie? (Enter movie title or press Enter to skip): ");
        String search = sc.nextLine().trim();
        if (!search.isEmpty()) {
            System.out.println("\nMatching records for \"" + search + "\":");
            int count = 0;
            for (Map<String, String> r : records) {
                if (r.get("movie_title").toLowerCase().contains(search.toLowerCase())) {
                    System.out.printf(" - Booking #%s: %s | %s tkts | Rs.%s | Date: %s | Status: %s\n",
                            r.get("booking_id"), r.get("movie_title"), r.get("number_of_tickets"),
                            r.get("gross_amount"), r.get("booking_date"), r.get("booking_status"));
                    count++;
                }
            }
            if (count == 0) System.out.println("No matching movies found.");
        }
    }

    private static void displayDailyBookingTrends() {
        System.out.println("\n-------------------------------------------------------------------------------");
        System.out.println("                DAY-BY-DAY DAILY BOOKING & TICKET TRENDS");
        System.out.println("-------------------------------------------------------------------------------");
        Map<String, Integer> dailyTkts = new TreeMap<>();
        Map<String, Integer> dailyOrders = new TreeMap<>();
        Map<String, Double> dailyRevenue = new TreeMap<>();

        for (Map<String, String> r : records) {
            String date = r.get("booking_date").split(" ")[0];
            int tkts = Integer.parseInt(r.getOrDefault("number_of_tickets", "0"));
            double rev = Double.parseDouble(r.getOrDefault("gross_amount", "0"));

            dailyTkts.put(date, dailyTkts.getOrDefault(date, 0) + tkts);
            dailyOrders.put(date, dailyOrders.getOrDefault(date, 0) + 1);
            dailyRevenue.put(date, dailyRevenue.getOrDefault(date, 0.0) + rev);
        }

        System.out.printf("%-12s %-15s %-15s %-18s %-20s\n", "Date", "Orders Placed", "Tickets Sold", "Daily Revenue (INR)", "Velocity Chart");
        System.out.println("-------------------------------------------------------------------------------");
        for (String d : dailyTkts.keySet()) {
            int tkts = dailyTkts.get(d);
            int orders = dailyOrders.get(d);
            double rev = dailyRevenue.get(d);

            StringBuilder bar = new StringBuilder();
            int bars = Math.min(25, (tkts + 1) / 2);
            for (int i = 0; i < bars; i++) bar.append("■");

            System.out.printf("%-12s %-15d %-15d Rs. %-14.2f %s\n", d, orders, tkts, rev, bar.toString());
        }
        System.out.println("-------------------------------------------------------------------------------");
        System.out.println("Insight: Nov 15 shows pre-opening peak booking rush with 49 tickets sold across 13 orders.");
    }

    private static void filterBookings(Scanner sc) {
        System.out.print("\nEnter Genre to filter (e.g. Action, Sci-Fi, Drama) or Language (e.g. Tamil, English): ");
        String term = sc.nextLine().trim().toLowerCase();

        int matches = 0;
        int tktSum = 0;
        double revSum = 0;

        System.out.println("\nMatching Results:");
        System.out.printf("%-6s %-25s %-10s %-10s %-8s %-10s\n", "ID", "Movie Title", "Genre", "Language", "Tickets", "Amount");
        System.out.println("-------------------------------------------------------------------------------");
        for (Map<String, String> r : records) {
            String g = r.get("genre").toLowerCase();
            String l = r.get("language").toLowerCase();
            if (g.contains(term) || l.contains(term)) {
                int t = Integer.parseInt(r.get("number_of_tickets"));
                double a = Double.parseDouble(r.get("gross_amount"));
                System.out.printf("%-6s %-25s %-10s %-10s %-8d Rs. %-8.2f\n",
                        r.get("booking_id"), r.get("movie_title"), r.get("genre"), r.get("language"), t, a);
                matches++;
                tktSum += t;
                revSum += a;
            }
        }
        System.out.println("-------------------------------------------------------------------------------");
        System.out.printf("Total Matches: %d | Combined Tickets: %d | Gross Total: Rs. %.2f\n", matches, tktSum, revSum);
    }

    private static void calculatePriceZScore(Scanner sc) {
        System.out.print("\nEnter a ticket price in INR to evaluate against distribution (e.g. 250): ");
        try {
            double price = Double.parseDouble(sc.nextLine().trim());
            List<Double> prices = new ArrayList<>();
            for (Map<String, String> r : records) {
                prices.add(Double.parseDouble(r.get("ticket_price")));
            }

            double sum = 0;
            for (double p : prices) sum += p;
            double mean = sum / prices.size();

            double var = 0;
            for (double p : prices) var += Math.pow(p - mean, 2);
            double std = Math.sqrt(var / prices.size());

            double zScore = (price - mean) / std;

            System.out.println("\n--- Statistical Evaluation of Ticket Price Rs. " + price + " ---");
            System.out.printf("Dataset Price Mean (μ)  : Rs. %.2f\n", mean);
            System.out.printf("Standard Deviation (σ)  : Rs. %.2f\n", std);
            System.out.printf("Calculated Z-Score (Z)  : %.4f\n", zScore);

            if (Math.abs(zScore) <= 1.0) {
                System.out.println("Interpretation: Typical/Standard pricing tier (within 1 std deviation).");
            } else if (Math.abs(zScore) <= 2.0) {
                System.out.println("Interpretation: Moderate deviation from average price (Premium / Matinee discount).");
            } else {
                System.out.println("Interpretation: Statistical Outlier / Ultra-Premium or promotional fare (> 2 std devs).");
            }
        } catch (Exception e) {
            System.out.println("Invalid numeric input!");
        }
    }

    private static void addNewBooking(Scanner sc) {
        System.out.println("\n--- Manual Input: Add Custom Booking Record to Session ---");
        try {
            System.out.print("Customer Name: ");
            String name = sc.nextLine().trim();
            System.out.print("Movie Title: ");
            String movie = sc.nextLine().trim();
            System.out.print("Genre: ");
            String genre = sc.nextLine().trim();
            System.out.print("Language: ");
            String lang = sc.nextLine().trim();
            System.out.print("Number of Tickets (1-10): ");
            int tkts = Integer.parseInt(sc.nextLine().trim());
            System.out.print("Ticket Price (INR): ");
            double price = Double.parseDouble(sc.nextLine().trim());

            double gross = tkts * price;
            int newId = 1000 + records.size() + 1;

            Map<String, String> newRow = new HashMap<>();
            newRow.put("booking_id", String.valueOf(newId));
            newRow.put("booking_date", "2026-11-16 12:00:00");
            newRow.put("customer_name", name);
            newRow.put("movie_id", "109");
            newRow.put("movie_title", movie);
            newRow.put("genre", genre);
            newRow.put("language", lang);
            newRow.put("duration_minutes", "150");
            newRow.put("screen_id", "1");
            newRow.put("screen_name", "Audi 1 - IMAX Laser");
            newRow.put("seating_capacity", "120");
            newRow.put("show_id", "515");
            newRow.put("show_date", "2026-11-16");
            newRow.put("show_time", "18:00:00");
            newRow.put("time_slot", "Evening");
            newRow.put("ticket_price", String.format(Locale.US, "%.2f", price));
            newRow.put("number_of_tickets", String.valueOf(tkts));
            newRow.put("gross_amount", String.format(Locale.US, "%.2f", gross));
            newRow.put("booking_status", "CONFIRMED");
            newRow.put("refund_amount", "0.00");
            newRow.put("net_amount", String.format(Locale.US, "%.2f", gross));
            newRow.put("show_occupancy_pct", "80.0");

            records.add(newRow);
            System.out.println("✓ Success! Added Booking #" + newId + " for " + name + " (" + tkts + " tickets, Rs. " + gross + ").");
            System.out.println("Dataset now contains " + records.size() + " records. Rerun rankings/stats to see live updates!");
        } catch (Exception e) {
            System.out.println("Failed to add booking due to invalid input: " + e.getMessage());
        }
    }

    private static void exportOutputToFile() {
        StringBuilder sb = new StringBuilder();
        sb.append("===============================================================================\n");
        sb.append("   VELALAR COLLEGE OF ENGINEERING AND TECHNOLOGY (AUTONOMOUS), ERODE - 12\n");
        sb.append("          DEPARTMENT OF INFORMATION TECHNOLOGY - ACADEMIC YEAR 2026-27\n");
        sb.append("       FOUNDATIONS OF DATA SCIENCE (25ITC31) - PROGRAM EXECUTION OUTPUT\n");
        sb.append("===============================================================================\n\n");

        sb.append("1. DATASET OVERVIEW\n");
        sb.append("-------------------------------------------------------------------------------\n");
        sb.append("Total Records Ingested : ").append(records.size()).append(" bookings\n");
        sb.append("Dataset Source File    : theatre_bookings.csv\n\n");

        sb.append("2. MOVIE POPULARITY (TICKETS SOLD RANKING)\n");
        sb.append("-------------------------------------------------------------------------------\n");
        Map<String, Integer> ticketCounts = new HashMap<>();
        for (Map<String, String> r : records) {
            String m = r.get("movie_title");
            int t = Integer.parseInt(r.getOrDefault("number_of_tickets", "0"));
            ticketCounts.put(m, ticketCounts.getOrDefault(m, 0) + t);
        }
        List<Map.Entry<String, Integer>> list = new ArrayList<>(ticketCounts.entrySet());
        list.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        int rank = 1;
        for (Map.Entry<String, Integer> e : list) {
            sb.append(String.format("Rank %d: %-28s -> %d tickets sold\n", rank++, e.getKey(), e.getValue()));
        }
        sb.append("\n3. DAILY BOOKING TRENDS\n");
        sb.append("-------------------------------------------------------------------------------\n");
        Map<String, Integer> dailyTkts = new TreeMap<>();
        for (Map<String, String> r : records) {
            String date = r.get("booking_date").split(" ")[0];
            int tkts = Integer.parseInt(r.getOrDefault("number_of_tickets", "0"));
            dailyTkts.put(date, dailyTkts.getOrDefault(date, 0) + tkts);
        }
        for (String d : dailyTkts.keySet()) {
            sb.append(String.format("%s : %d tickets sold\n", d, dailyTkts.get(d)));
        }

        try {
            Files.writeString(Paths.get("program_execution_output.txt"), sb.toString());
            System.out.println("✓ Successfully generated text report: program_execution_output.txt");
        } catch (Exception e) {
            System.out.println("Error writing output file: " + e.getMessage());
        }
    }
}
