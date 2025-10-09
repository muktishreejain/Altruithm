import pandas as pd
from sqlalchemy import create_engine

username = "root"           # MySQL username
password = "tiger"   # MySQL password
host = "localhost"          # or the IP address of your MySQL server
port = 3306                 # default MySQL port
database = "althruithm"         # the database name 


engine = create_engine(f"mysql+mysqlconnector://{username}:{password}@{host}:{port}/{database}")

df1 = pd.read_csv("CharityData_NonProfit_cleaned_financial.csv")
df2 = pd.read_csv("CharityData_NonProfit_cleaned.csv")

df2.drop(columns=["STATE","EIN"], inplace=True)
df2.to_csv("CharityData_NonProfit_cleaned.csv", index=False)


df1.to_sql(name="financials", con=engine, if_exists="replace", index=False)


df2.to_sql(name="main_dataset", con=engine, if_exists="replace", index=False)



