"""
================================================================================
FOUNDATIONS OF DATA SCIENCE (Course Code: 25ITC31)
SCENARIO 30: MOVIE THEATRE TICKET BOOKING & BOX OFFICE ANALYSIS SYSTEM
================================================================================
Academic Year: 2026-2027 (ODD) | Class: 25IT3A & 25IT3B
Department of Information Technology, Velalar College of Engineering & Technology

Modules Implemented:
- Unit 1: Data Description & Descriptive Statistics (Mean, Median, Mode, Variance, Z-Scores)
- Unit 2: Python Environment & CSV Dataset Ingestion
- Unit 3: Data Wrangling with NumPy & Pandas (Aggregation, GroupBy, Pivot Tables)
- Unit 4: Data Cleaning, Imputation, Duplicate Handling, Outliers & Feature Engineering
- Unit 5: Exploratory Data Analysis (EDA) & Visualizations (Matplotlib & Seaborn)
================================================================================
"""

import os
import sys
import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns

# Set visual aesthetic styling
sns.set_theme(style="whitegrid", palette="muted")
plt.rcParams['font.sans-serif'] = 'DejaVu Sans'
plt.rcParams['figure.dpi'] = 300

OUTPUT_DIR = os.path.join(os.path.dirname(__file__), "visualizations")
os.makedirs(OUTPUT_DIR, exist_ok=True)


def load_dataset(filepath="theatre_bookings.csv"):
    """
    Unit 2: File Handling & Dataset Exploration
    Ingests CSV export of database booking and show records.
    """
    if not os.path.exists(filepath):
        # Fallback to local script directory
        filepath = os.path.join(os.path.dirname(__file__), filepath)
    
    print(f"[INFO] Ingesting theatre dataset from: {filepath}")
    df = pd.read_csv(filepath)
    print(f"[INFO] Dataset Loaded Successfully: {df.shape[0]} rows, {df.shape[1]} columns.\n")
    return df


def data_cleaning_and_preprocessing(df):
    """
    Unit 4: Data Cleaning and Transformation Techniques
    - Handling Missing Values
    - Duplicate Detection and Removal
    - Data Type Conversions (Datetime, Categorical)
    - Outlier Detection via IQR
    - Feature Engineering: Net Amount, Occupancy Rate %, Booking Day of Week
    """
    print("=" * 70)
    print(" UNIT 4: DATA CLEANING, PREPROCESSING & TRANSFORMATION ")
    print("=" * 70)
    
    # 1. Missing Value Audit
    missing_count = df.isnull().sum().sum()
    print(f"Missing Values Detected: {missing_count}")
    if missing_count > 0:
        df['refund_amount'] = df['refund_amount'].fillna(0.0)
        df.dropna(subset=['booking_id', 'show_id'], inplace=True)

    # 2. Duplicate Detection
    duplicates = df.duplicated(subset=['booking_id']).sum()
    print(f"Duplicate Booking IDs Found: {duplicates}")
    if duplicates > 0:
        df.drop_duplicates(subset=['booking_id'], inplace=True)

    # 3. Data Type Conversions
    df['booking_date'] = pd.to_datetime(df['booking_date'])
    df['show_date'] = pd.to_datetime(df['show_date'])
    df['ticket_price'] = df['ticket_price'].astype(float)
    df['number_of_tickets'] = df['number_of_tickets'].astype(int)
    df['gross_amount'] = df['gross_amount'].astype(float)
    df['refund_amount'] = df['refund_amount'].astype(float)
    df['net_amount'] = df['gross_amount'] - df['refund_amount']

    # 4. Feature Engineering
    df['booking_day_of_week'] = df['booking_date'].dt.day_name()
    df['lead_time_days'] = (df['show_date'] - df['booking_date']).dt.total_seconds() / (24 * 3600)
    
    # 5. Outlier Detection using Interquartile Range (IQR) on gross_amount
    q1 = df['gross_amount'].quantile(0.25)
    q3 = df['gross_amount'].quantile(0.75)
    iqr = q3 - q1
    lower_bound = q1 - 1.5 * iqr
    upper_bound = q3 + 1.5 * iqr
    outliers = df[(df['gross_amount'] < lower_bound) | (df['gross_amount'] > upper_bound)]
    print(f"IQR Outlier Analysis on Gross Booking Amount:")
    print(f"  Q1: ₹{q1:.2f} | Q3: ₹{q3:.2f} | IQR: ₹{iqr:.2f}")
    print(f"  Valid Range: [₹{lower_bound:.2f}, ₹{upper_bound:.2f}]")
    print(f"  Statistical Outliers Detected: {len(outliers)} records.\n")

    return df


def descriptive_statistics_and_z_scores(df):
    """
    Unit 1: Data Description & Statistical Measures
    Calculates Central Tendency (Mean, Median, Mode), Dispersion (Variance, Std Dev, Range, IQR),
    and Standard Z-Scores for Gross Amount and Occupancy.
    """
    print("=" * 70)
    print(" UNIT 1: DESCRIPTIVE STATISTICS & Z-SCORE COMPUTATION ")
    print("=" * 70)
    
    numerical_cols = ['ticket_price', 'number_of_tickets', 'gross_amount', 'net_amount', 'show_occupancy_pct']
    stats_summary = []

    for col in numerical_cols:
        col_data = df[col]
        mean_val = np.mean(col_data)
        median_val = np.median(col_data)
        mode_val = col_data.mode()[0]
        var_val = np.var(col_data, ddof=1)
        std_val = np.std(col_data, ddof=1)
        range_val = np.ptp(col_data)
        iqr_val = np.percentile(col_data, 75) - np.percentile(col_data, 25)

        stats_summary.append({
            'Metric': col,
            'Mean': round(mean_val, 2),
            'Median': round(median_val, 2),
            'Mode': round(mode_val, 2),
            'Std_Dev': round(std_val, 2),
            'Variance': round(var_val, 2),
            'Range': round(range_val, 2),
            'IQR': round(iqr_val, 2)
        })

    stats_df = pd.DataFrame(stats_summary)
    print(stats_df.to_string(index=False))
    print()

    # Z-Score computation for Gross Revenue (Normal Distribution assessment)
    df['gross_amount_zscore'] = (df['gross_amount'] - df['gross_amount'].mean()) / df['gross_amount'].std()
    print("Top 5 Bookings by Standardized Z-Score (Abnormal / High Value Transactions):")
    top_z = df[['booking_id', 'customer_name', 'movie_title', 'gross_amount', 'gross_amount_zscore']].sort_values(
        by='gross_amount_zscore', ascending=False).head(5)
    print(top_z.to_string(index=False))
    print()

    return df, stats_df


def data_wrangling_and_pivots(df):
    """
    Unit 3: Data Wrangling with Pandas
    - Grouping & Aggregations
    - Multi-level hierarchical grouping
    - Pivot Tables: Screen Name vs Genre Occupancy
    """
    print("=" * 70)
    print(" UNIT 3: DATA WRANGLING & PIVOT TABLES ")
    print("=" * 70)

    # Movie-wise Box Office Summary Table
    movie_summary = df.groupby('movie_title').agg(
        Total_Bookings=('booking_id', 'count'),
        Tickets_Sold=('number_of_tickets', 'sum'),
        Gross_Revenue=('gross_amount', 'sum'),
        Total_Refunds=('refund_amount', 'sum'),
        Net_Revenue=('net_amount', 'sum'),
        Avg_Occupancy=('show_occupancy_pct', 'mean')
    ).reset_index().sort_values(by='Net_Revenue', ascending=False)

    print("Movie-Wise Box Office Performance Summary:")
    print(movie_summary.to_string(index=False))
    print()

    # Pivot Table: Screen Name vs Genre (Average Occupancy %)
    pivot_occupancy = df.pivot_table(
        index='screen_name',
        columns='genre',
        values='show_occupancy_pct',
        aggfunc='mean',
        fill_value=0.0
    ).round(2)

    print("Pivot Table: Screen vs Genre Average Occupancy (%):")
    print(pivot_occupancy)
    print()

    return movie_summary, pivot_occupancy


def generate_visualizations(df, movie_summary, pivot_occupancy):
    """
    Unit 5: Exploratory Data Analysis (EDA) and Visualization Techniques
    Generates 6 publication-ready charts using Matplotlib and Seaborn.
    """
    print("=" * 70)
    print(" UNIT 5: GENERATING MATPLOTLIB & SEABORN VISUALIZATIONS ")
    print("=" * 70)

    # -------------------------------------------------------------------------
    # Chart 1: Movie-Wise Box Office Gross vs Net Revenue (Bar Chart)
    # -------------------------------------------------------------------------
    plt.figure(figsize=(10, 6))
    x = np.arange(len(movie_summary))
    width = 0.35
    plt.bar(x - width/2, movie_summary['Gross_Revenue'], width, label='Gross Revenue (₹)', color='#1f77b4', edgecolor='black')
    plt.bar(x + width/2, movie_summary['Net_Revenue'], width, label='Net Revenue (₹)', color='#2ca02c', edgecolor='black')
    plt.xticks(x, movie_summary['movie_title'], rotation=25, ha='right', fontsize=9)
    plt.ylabel("Revenue in INR (₹)", fontsize=11, fontweight='bold')
    plt.title("Figure 1: Box Office Gross vs. Net Revenue by Movie", fontsize=13, fontweight='bold', pad=15)
    plt.legend(frameon=True)
    plt.tight_layout()
    chart1_path = os.path.join(OUTPUT_DIR, "1_movie_revenue_comparison.png")
    plt.savefig(chart1_path)
    plt.close()
    print(f"Saved: {chart1_path}")

    # -------------------------------------------------------------------------
    # Chart 2: Revenue Contribution by Genre (Pie / Donut Chart)
    # -------------------------------------------------------------------------
    genre_rev = df.groupby('genre')['net_amount'].sum()
    plt.figure(figsize=(8, 8))
    colors = ['#ff9999', '#66b3ff', '#99ff99', '#ffcc99', '#c2c2f0']
    plt.pie(genre_rev, labels=genre_rev.index, autopct='%1.1f%%', startangle=140,
            colors=colors[:len(genre_rev)], wedgeprops={'edgecolor': 'white', 'linewidth': 2})
    centre_circle = plt.Circle((0,0), 0.70, fc='white')
    fig = plt.gcf()
    fig.gca().add_artist(centre_circle)
    plt.title("Figure 2: Net Revenue Contribution by Film Genre", fontsize=13, fontweight='bold', pad=15)
    plt.tight_layout()
    chart2_path = os.path.join(OUTPUT_DIR, "2_genre_revenue_distribution.png")
    plt.savefig(chart2_path)
    plt.close()
    print(f"Saved: {chart2_path}")

    # -------------------------------------------------------------------------
    # Chart 3: Theatre Occupancy Rate Across Screens (Seaborn Box Plot)
    # -------------------------------------------------------------------------
    plt.figure(figsize=(10, 6))
    sns.boxplot(x='screen_name', y='show_occupancy_pct', data=df, palette='Set2')
    sns.stripplot(x='screen_name', y='show_occupancy_pct', data=df, color='black', alpha=0.5, jitter=0.2)
    plt.xticks(rotation=15, ha='right')
    plt.xlabel("Screen Name", fontsize=11, fontweight='bold')
    plt.ylabel("Show Occupancy Rate (%)", fontsize=11, fontweight='bold')
    plt.title("Figure 3: Occupancy Rate Dispersion Across Multiplex Screens", fontsize=13, fontweight='bold', pad=15)
    plt.tight_layout()
    chart3_path = os.path.join(OUTPUT_DIR, "3_screen_occupancy_boxplot.png")
    plt.savefig(chart3_path)
    plt.close()
    print(f"Saved: {chart3_path}")

    # -------------------------------------------------------------------------
    # Chart 4: Correlation Matrix Heatmap (Multivariate Analysis)
    # -------------------------------------------------------------------------
    plt.figure(figsize=(8, 6))
    numeric_df = df[['ticket_price', 'number_of_tickets', 'gross_amount', 'refund_amount', 'net_amount', 'show_occupancy_pct']]
    corr_matrix = numeric_df.corr()
    sns.heatmap(corr_matrix, annot=True, cmap='coolwarm', fmt=".2f", linewidths=1, square=True)
    plt.title("Figure 4: Pearson Correlation Matrix Heatmap", fontsize=13, fontweight='bold', pad=15)
    plt.tight_layout()
    chart4_path = os.path.join(OUTPUT_DIR, "4_correlation_heatmap.png")
    plt.savefig(chart4_path)
    plt.close()
    print(f"Saved: {chart4_path}")

    # -------------------------------------------------------------------------
    # Chart 5: Ticket Price vs Occupancy Rate (Scatter Plot with Trend)
    # -------------------------------------------------------------------------
    plt.figure(figsize=(9, 6))
    sns.regplot(x='ticket_price', y='show_occupancy_pct', data=df,
                scatter_kws={'alpha':0.7, 'color':'#d62728', 's':70}, line_kws={'color':'#1f77b4', 'linewidth':2})
    plt.xlabel("Ticket Price (₹)", fontsize=11, fontweight='bold')
    plt.ylabel("Show Occupancy (%)", fontsize=11, fontweight='bold')
    plt.title("Figure 5: Price Elasticity - Ticket Price vs. Occupancy Rate", fontsize=13, fontweight='bold', pad=15)
    plt.tight_layout()
    chart5_path = os.path.join(OUTPUT_DIR, "5_price_vs_occupancy_scatter.png")
    plt.savefig(chart5_path)
    plt.close()
    print(f"Saved: {chart5_path}")

    # -------------------------------------------------------------------------
    # Chart 6: Time Slot Popularity (Bar Chart)
    # -------------------------------------------------------------------------
    plt.figure(figsize=(8, 5))
    time_slot_agg = df.groupby('time_slot')['show_occupancy_pct'].mean().reindex(['Morning', 'Matinee', 'Evening', 'Night'])
    sns.barplot(x=time_slot_agg.index, y=time_slot_agg.values, palette='viridis')
    plt.xlabel("Show Time Slot", fontsize=11, fontweight='bold')
    plt.ylabel("Average Occupancy Rate (%)", fontsize=11, fontweight='bold')
    plt.title("Figure 6: Average Occupancy Rate by Time Slot", fontsize=13, fontweight='bold', pad=15)
    plt.tight_layout()
    chart6_path = os.path.join(OUTPUT_DIR, "6_timeslot_occupancy.png")
    plt.savefig(chart6_path)
    plt.close()
    print(f"Saved: {chart6_path}")

    print("\n[SUCCESS] All 6 visualizations generated and exported to 'visualizations/' folder.")


def main():
    csv_file = "theatre_bookings.csv"
    df = load_dataset(csv_file)
    df_cleaned = data_cleaning_and_preprocessing(df)
    df_stats, stats_df = descriptive_statistics_and_z_scores(df_cleaned)
    movie_summary, pivot_occupancy = data_wrangling_and_pivots(df_stats)
    
    try:
        generate_visualizations(df_stats, movie_summary, pivot_occupancy)
    except Exception as e:
        print(f"[NOTE] Matplotlib/Seaborn visualization generation skipped or encountered: {e}")
        print("Dataset wrangling, descriptive stats, and pivot analyses completed successfully.")


if __name__ == "__main__":
    main()
