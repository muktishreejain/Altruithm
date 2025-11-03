import pandas as pd
df1_charityData = pd.read_csv("CLEAN_charity_data.csv")
df2_grants = pd.read_csv("grants_final.csv")
df3_nonProfits = pd.read_csv("Non-profits_final.csv")


df1_charityData["name"]=df1_charityData["name"].str.upper()
df1_charityData.rename(columns={"name":"NAME"},inplace=True)
#print(df1_charityData["NAME"])
#print(df2_grants.head())
df1_subset = df1_charityData[["ascore","category","description","ein","tot_exp","fund_eff","fscore","tot_rev","score","state","size"]]

df2_subset = df3_nonProfits[["CITY","ZIP","STATUS","NAME","impact_score","impact_efficiency","INCOME_AMT","REVENUE_AMT"]]
df13_merged = pd.merge(df1_charityData,df3_nonProfits, on ="NAME", how = "inner")
df13_merged_cleaned_financial = df1_subset.merge(df2_subset,on = "NAME",how = "inner")



print(df13_merged_cleaned_financial.head())
df13_merged_cleaned_financial.to_csv("CharityData_NonProfit_cleaned.csv",index = False)