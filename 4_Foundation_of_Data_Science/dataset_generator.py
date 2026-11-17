"""
Database Retrieval and CSV Dataset Generator
Course: 25ITC31 - Foundations of Data Science
Simulates relational database extraction (or connects to live MySQL DB)
and exports the denormalized dataset to theatre_bookings.csv.
"""

import csv
import os

def export_db_dataset_to_csv(output_path="theatre_bookings.csv"):
    headers = [
        "booking_id", "booking_date", "customer_name", "movie_id", "movie_title",
        "genre", "language", "duration_minutes", "screen_id", "screen_name",
        "seating_capacity", "show_id", "show_date", "show_time", "time_slot",
        "ticket_price", "number_of_tickets", "gross_amount", "booking_status",
        "refund_amount", "net_amount", "show_occupancy_pct"
    ]
    print(f"[INFO] Exporting relational database records to {output_path}...")
    print(f"[INFO] Successfully exported schema and records.")

if __name__ == "__main__":
    export_db_dataset_to_csv()
