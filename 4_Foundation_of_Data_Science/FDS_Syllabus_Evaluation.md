# CATEGORY 4: FOUNDATIONS OF DATA SCIENCE (Course Code: 25ITC31)
## Detailed Requirements Evaluation & Syllabus Alignment Document

### Project Title:
**Movie Theatre Ticket Booking and Box Office Analysis System**
*Assigned Register Numbers:* 30, 60, 90, 120  
**Institution:** Velalar College of Engineering and Technology, Erode - 12  
**Department:** Information Technology | **Class:** 25IT3A & 25IT3B

---

## 1. Executive Summary
The business value of modern cinema multiplexes hinges on converting raw booking transactions into strategic insights. The Foundations of Data Science (FDS) component ingests transaction and occupancy data from the database, performs data cleaning, data wrangling, and statistical exploratory data analysis (EDA), and generates visualizations that uncover film popularity, screen utilization patterns, and revenue trends.

---

## 2. Direct Syllabus Mapping (25ITC31: Foundations of Data Science)

| Syllabus Unit | Core Theoretical Concepts in Syllabus | Project Implementation / Python Deliverables |
| :--- | :--- | :--- |
| **Unit 1: Data Science Fundamentals & Data Description** | Data Science process, Types of data/variables, Tables & graphs, Measures of central tendency (Mean, Median, Mode), Variability (Variance, Std Dev, Range, IQR), Normal distributions and standard $Z$-scores. | - `box_office_analysis.py`: Computes full summary statistics (Mean, Median, Mode, Std Dev, Variance, IQR, Range) for `ticket_price`, `number_of_tickets`, `gross_amount`, `net_amount`, and `show_occupancy_pct`.<br>- Computes standard $Z$-scores to isolate high-value outlier bookings. |
| **Unit 2: Python Foundations for Data Analysis** | Python Environment, Variables, Data Types, Control flow, Data Structures (Lists, Dictionaries), File Handling, Reading/Writing CSV files, Importing Libraries. | - Script `box_office_analysis.py` with CSV ingestion pipeline via Pandas.<br>- Data structures for statistical aggregations and dictionary-based summaries. |
| **Unit 3: Data Wrangling** | NumPy Arrays, computations on arrays, Pandas Series & DataFrames, Indexing & Selection, Aggregation & Grouping, Pivot Tables. | - NumPy vector operations on revenue and ticket prices.<br>- Pandas `groupby('movie_title')` computing multi-attribute aggregations.<br>- Pivot table generated for `screen_name` vs `genre` occupancy rates. |
| **Unit 4: Data Cleaning and Transformation Techniques** | Data quality, Handling missing values, Duplicate detection & removal, Data type conversion, Outlier identification (IQR), Normalization/Scaling, Feature creation. | - Missing value handling (`fillna(0.0)` for refunds).<br>- Duplicate removal on `booking_id`.<br>- Datetime parsing (`pd.to_datetime`).<br>- IQR-based outlier detection for ticket sales.<br>- Feature engineering: `net_amount = gross_amount - refund_amount`, `booking_day_of_week`, `lead_time_days`. |
| **Unit 5: Exploratory Data Analysis (EDA) & Visualization Techniques** | Principles of effective visualization, Matplotlib charts (Line, Bar, Pie, Hist, Scatter), Seaborn charts (Box plots, Heat maps, Pair plots), Correlation analysis, Time series visualization. | - Six publication-grade charts exported to `visualizations/`:<br>1. Movie-wise Gross vs Net Revenue (Grouped Bar Chart)<br>2. Net Revenue Contribution by Film Genre (Donut Chart)<br>3. Multiplex Screen Occupancy Dispersion (Seaborn Box Plot)<br>4. Multivariate Pearson Correlation Heatmap<br>5. Ticket Price vs Occupancy Elasticity (Scatter Plot with Regression Trend)<br>6. Average Occupancy Rate by Time Slot (Bar Chart). |

---

## 3. Evaluation of Scenario 30 Requirements under Data Science

### 3.1 Data Characteristics & Variable Taxonomy
- **Categorical (Nominal):** `movie_title`, `genre`, `language`, `screen_name`, `booking_status`, `time_slot`.
- **Temporal:** `booking_date`, `show_date`, `show_time`.
- **Numerical (Discrete):** `number_of_tickets`, `duration_minutes`, `seating_capacity`.
- **Numerical (Continuous):** `ticket_price`, `gross_amount`, `refund_amount`, `net_amount`, `show_occupancy_pct`.

### 3.2 Key Data Science Findings & Analytical Formulations
1. **Occupancy Rate Formula:**
   $$\text{Occupancy Rate (\%)} = \left(\frac{\text{Seating Capacity} - \text{Available Seats}}{\text{Seating Capacity}}\right) \times 100$$
2. **Standard Score ($Z$-Score) Formula:**
   $$Z = \frac{X - \mu}{\sigma}$$
   Used to identify extraordinary booking anomalies (e.g. bulk corporate bookings where $Z > +2.5$).
3. **Interquartile Range (IQR) Outlier Rule:**
   $$\text{IQR} = Q_3 - Q_1$$
   $$\text{Lower Bound} = Q_1 - 1.5 \times \text{IQR}, \quad \text{Upper Bound} = Q_3 + 1.5 \times \text{IQR}$$

---

## 4. Course Outcomes (CO) Addressed
- **CO1:** Analyze datasets using descriptive statistical measures and data visualization techniques.
- **CO2:** Develop Python programs using fundamental programming constructs, data structures, and file operations.
- **CO3:** Apply NumPy and Pandas libraries to organize, manipulate, summarize, and integrate datasets.
- **CO4:** Perform data cleaning and transformation techniques to improve data quality and prepare datasets for analysis.
- **CO5:** Utilize exploratory data analysis and visualization methods to summarize data characteristics and uncover hidden business insights.
