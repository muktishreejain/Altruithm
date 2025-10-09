import pandas as pd
from sqlalchemy import create_engine

# Replace these values with your MySQL credentials
username = "root"           # your MySQL username
password = "tiger"   # your MySQL password
host = "localhost"          # or the IP address of your MySQL server
port = 3306                 # default MySQL port
database = "althruithm"         # the database name (must exist already)

# Create SQLAlchemy engine
engine = create_engine(f"mysql+mysqlconnector://{username}:{password}@{host}:{port}/{database}")

df1 = pd.read_csv("CharityData_NonProfit_cleaned_financial.csv")
df2 = pd.read_csv("CharityData_NonProfit_cleaned.csv")

df2.drop(columns=["STATE","EIN"], inplace=True)
df2.to_csv("CharityData_NonProfit_cleaned.csv", index=False)


# Write df1 to MySQL
df1.to_sql(name="financials", con=engine, if_exists="replace", index=False)

# Write df2 to MySQL
df2.to_sql(name="main_dataset", con=engine, if_exists="replace", index=False)


