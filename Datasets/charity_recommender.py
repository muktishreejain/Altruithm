# charity_recommender.py

import pandas as pd
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity

# Step 1: Load the dataset
# Make sure 'charity.csv' is in the same folder as this Python file
df = pd.read_csv('charity.csv')

# Step 2: Preview the data
print("Dataset Loaded Successfully! Here are the first 5 entries:\n")
print(df.head())

# Step 3: Combine relevant text columns into one string
# (Change column names below based on your CSV headers)
df['combined'] = df['Charity Name'].astype(str) + " " + df['Description'].astype(str) + " " + df['Cause'].astype(str)

# Step 4: Convert text data into TF-IDF matrix
vectorizer = TfidfVectorizer(stop_words='english')
tfidf_matrix = vectorizer.fit_transform(df['combined'])

# Step 5: Compute similarity matrix
cosine_sim = cosine_similarity(tfidf_matrix, tfidf_matrix)

# Step 6: Function to get recommendations
def recommend_charity(charity_name, top_n=5):
    # Check if the charity exists
    if charity_name not in df['Charity Name'].values:
        print(f"'{charity_name}' not found in dataset.")
        return []

    # Get index of the charity
    idx = df[df['Charity Name'] == charity_name].index[0]
    sim_scores = list(enumerate(cosine_sim[idx]))
    sim_scores = sorted(sim_scores, key=lambda x: x[1], reverse=True)
    sim_scores = sim_scores[1:top_n+1]  # Exclude the charity itself

    charity_indices = [i[0] for i in sim_scores]
    recommended = df.iloc[charity_indices][['Charity Name', 'Description', 'Cause']]
    return recommended

# Step 7: Example run
print("\nExample Recommendation:")
example_charity = input("Enter a charity name to find similar ones: ")
recommendations = recommend_charity(example_charity)

if len(recommendations) > 0:
    print("\nTop Similar Charities:\n")
    print(recommendations)
