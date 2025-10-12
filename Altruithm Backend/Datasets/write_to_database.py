import pandas as pd
import mysql.connector
from mysql.connector import Error
import numpy as np

# Database configuration
DB_CONFIG = {
    'host': 'localhost',
    'user': 'root',
    'password': 'DarkSorcerer@014', 
    'database': 'altruithm_db'
}

def create_database_connection():
    """Create database connection"""
    try:
        connection = mysql.connector.connect(**DB_CONFIG)
        if connection.is_connected():
            print("[OK] Successfully connected to MySQL database")
            return connection
    except Error as e:
        print(f"[ERROR] Error connecting to MySQL: {e}")
        return None

def create_tables(connection):
    """Create tables with proper schema"""
    cursor = connection.cursor()
    
    # Table 1: charity_basic (from CharityData_NonProfit_cleaned.csv)
    create_basic_table = """
    CREATE TABLE IF NOT EXISTS charity_basic (
        id INT AUTO_INCREMENT PRIMARY KEY,
        ascore DOUBLE,
        category VARCHAR(255),
        description TEXT,
        tot_exp DOUBLE,
        fund_eff DOUBLE,
        fscore DOUBLE,
        NAME VARCHAR(500),
        tot_rev DOUBLE,
        score DOUBLE,
        state VARCHAR(10),
        size VARCHAR(50),
        EIN BIGINT,
        CITY VARCHAR(255),
        ZIP VARCHAR(20),
        STATUS INT,
        impact_score VARCHAR(50),
        impact_efficiency DOUBLE,
        INCOME_AMT DOUBLE,
        REVENUE_AMT DOUBLE,
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        INDEX idx_ein (ein),
        INDEX idx_name (NAME(255)),
        INDEX idx_score (score)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
    """
    
    # Table 2: charity_financial (from CharityData_NonProfit_cleaned_financial.csv)
    create_financial_table = """
    CREATE TABLE IF NOT EXISTS charity_financial (
        id INT AUTO_INCREMENT PRIMARY KEY,
        ascore DOUBLE,
        category VARCHAR(255),
        tot_exp DOUBLE,
        admin_exp_p DOUBLE,
        fund_eff DOUBLE,
        fund_exp_p DOUBLE,
        program_exp_p DOUBLE,
        leader_comp_p DOUBLE,
        program_exp DOUBLE,
        fund_exp DOUBLE,
        fscore DOUBLE,
        NAME VARCHAR(500),
        tot_rev DOUBLE,
        subcategory VARCHAR(255),
        score DOUBLE,
        size VARCHAR(50),
        EIN BIGINT,
        STATUS INT,
        impact_score VARCHAR(50),
        impact_efficiency DOUBLE,
        INCOME_AMT DOUBLE,
        REVENUE_AMT BIGINT,
        NTEE_CD VARCHAR(20),
        ASSET_AMT DOUBLE,
        ACCT_PD BIGINT,
        PF_FILING_REQ_CD INT,
        FILING_REQ_CD INT,
        INCOME_CD INT,
        ASSET_CD INT,
        FOUNDATION INT,
        DEDUCTIBILITY DOUBLE,
        RULING DOUBLE,
        CLASSIFICATION DOUBLE,
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        INDEX idx_ein (EIN),
        INDEX idx_name (NAME(255)),
        INDEX idx_score (score),
        INDEX idx_category (category)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
    """
    
    try:
        cursor.execute(create_basic_table)
        print("[OK] Created/verified charity_basic table")
        
        cursor.execute(create_financial_table)
        print("[OK] Created/verified charity_financial table")
        
        connection.commit()
    except Error as e:
        print(f"[ERROR] Error creating tables: {e}")
    finally:
        cursor.close()

def clean_dataframe(df):
    """Clean dataframe for MySQL import"""
    # Replace NaN with None for MySQL NULL
    df = df.replace({np.nan: None})
    
    # Convert inf to None
    df = df.replace([np.inf, -np.inf], None)
    
    # Strip whitespace from string columns
    for col in df.select_dtypes(include=['object']).columns:
        df[col] = df[col].apply(lambda x: x.strip() if isinstance(x, str) else x)
    
    return df

def import_basic_data(connection, csv_file):
    """Import CharityData_NonProfit_cleaned.csv"""
    try:
        print(f"\n[READING] {csv_file}...")
        df = pd.read_csv(csv_file)
        df = clean_dataframe(df)
        
        print(f"   Found {len(df)} rows, {len(df.columns)} columns")
        
        cursor = connection.cursor()
        
        # Prepare INSERT query
        columns = ', '.join(df.columns)
        placeholders = ', '.join(['%s'] * len(df.columns))
        insert_query = f"INSERT INTO charity_basic ({columns}) VALUES ({placeholders})"
        
        # Insert data in batches
        batch_size = 100
        total_inserted = 0
        
        for i in range(0, len(df), batch_size):
            batch = df.iloc[i:i+batch_size]
            data = [tuple(row) for row in batch.values]
            cursor.executemany(insert_query, data)
            connection.commit()
            total_inserted += len(batch)
            print(f"   [PROGRESS] Inserted {total_inserted}/{len(df)} rows...", end='\r')
        
        print(f"\n[OK] Successfully imported {total_inserted} rows into charity_basic")
        cursor.close()
        
    except Error as e:
        print(f"[ERROR] Error importing basic data: {e}")
    except Exception as e:
        print(f"[ERROR] General error: {e}")

def import_financial_data(connection, csv_file):
    """Import CharityData_NonProfit_cleaned_financial.csv"""
    try:
        print(f"\n[READING] {csv_file}...")
        df = pd.read_csv(csv_file)
        df = clean_dataframe(df)
        
        print(f"   Found {len(df)} rows, {len(df.columns)} columns")
        
        cursor = connection.cursor()
        
        # Prepare INSERT query
        columns = ', '.join(df.columns)
        placeholders = ', '.join(['%s'] * len(df.columns))
        insert_query = f"INSERT INTO charity_financial ({columns}) VALUES ({placeholders})"
        
        # Insert data in batches
        batch_size = 100
        total_inserted = 0
        
        for i in range(0, len(df), batch_size):
            batch = df.iloc[i:i+batch_size]
            data = [tuple(row) for row in batch.values]
            cursor.executemany(insert_query, data)
            connection.commit()
            total_inserted += len(batch)
            print(f"   [PROGRESS] Inserted {total_inserted}/{len(df)} rows...", end='\r')
        
        print(f"\n[OK] Successfully imported {total_inserted} rows into charity_financial")
        cursor.close()
        
    except Error as e:
        print(f"[ERROR] Error importing financial data: {e}")
    except Exception as e:
        print(f"[ERROR] General error: {e}")

def verify_import(connection):
    """Verify the imported data"""
    cursor = connection.cursor()
    
    try:
        cursor.execute("SELECT COUNT(*) FROM charity_basic")
        basic_count = cursor.fetchone()[0]
        print(f"\n[DATA] charity_basic table: {basic_count} rows")
        
        cursor.execute("SELECT COUNT(*) FROM charity_financial")
        financial_count = cursor.fetchone()[0]
        print(f"[DATA] charity_financial table: {financial_count} rows")
        
        # Show sample data
        cursor.execute("SELECT NAME, score, tot_rev FROM charity_basic LIMIT 3")
        print("\n[SAMPLE] Sample data from charity_basic:")
        for row in cursor.fetchall():
            print(f"   - {row[0]}: Score={row[1]}, Revenue={row[2]}")
            
    except Error as e:
        print(f"[ERROR] Error verifying data: {e}")
    finally:
        cursor.close()

def main():
    """Main execution"""
    print("=" * 60)
    print("Altruithm Database Import Script")
    print("=" * 60)
    
    # File paths - UPDATE THESE
    basic_csv = "CharityData_NonProfit_cleaned.csv"
    financial_csv = "CharityData_NonProfit_cleaned_financial.csv"
    
    # Connect to database
    connection = create_database_connection()
    if not connection:
        return
    
    try:
        # Create tables
        print("\n[SETUP] Creating tables...")
        create_tables(connection)
        
        # Import data
        print("\n[IMPORT] Importing data...")
        import_basic_data(connection, basic_csv)
        import_financial_data(connection, financial_csv)
        
        # Verify
        verify_import(connection)
        
        print("\n" + "=" * 60)
        print("[SUCCESS] DATABASE IMPORT COMPLETED SUCCESSFULLY!")
        print("=" * 60)
        print("\n[NEXT STEPS]")
        print("   1. Update your Spring Boot application.properties")
        print("   2. Create JPA entities matching these tables")
        print("   3. Test your fraud detection API with real data")
        
    finally:
        if connection.is_connected():
            connection.close()
            print("\n[CLOSED] Database connection closed")

if __name__ == "__main__":
    main()