import pandas as pd

df1_charityData = pd.read_csv("CLEAN_charity_data.csv")
df2_grants = pd.read_csv("grants_final.csv")
df3_nonProfits = pd.read_csv("Non-profits_final.csv")


df1_charityData["name"]=df1_charityData["name"].str.upper()
df1_charityData.rename(columns={"name":"NAME"},inplace=True)


df1_subset = df1_charityData[["ascore","category","tot_exp","admin_exp_p","fund_eff","fund_exp_p","program_exp_p","leader_comp_p","program_exp","fund_exp","fscore","NAME","tot_rev","subcategory","score","size",]]
df2_subset = df3_nonProfits[["EIN","NAME","STATUS","impact_score","impact_efficiency","INCOME_AMT","REVENUE_AMT","NTEE_CD","ASSET_AMT","ACCT_PD","PF_FILING_REQ_CD","FILING_REQ_CD","INCOME_CD","ASSET_CD","FOUNDATION","DEDUCTIBILITY","RULING","CLASSIFICATION"]]

df13_merged = pd.merge(df1_charityData,df3_nonProfits, on ="NAME", how = "inner")
df13_merged_cleaned_financial = df1_subset.merge(df2_subset,on = "NAME",how = "inner")

print(df13_merged_cleaned_financial.head())
df13_merged_cleaned_financial.to_csv("CharityData_NonProfit_cleaned_financial.csv",index = False)