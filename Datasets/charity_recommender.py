# charity_recommender.py
import sys
import pandas as pd
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity

# Load dataset
df = pd.read_csv( "CharityData_NonProfit_cleaned_FINAL.csv")

# Combine textual fields
df['combined'] = (
    df['name'].astype(str) + " " +
    df['description'].astype(str) + " " +
    df['category'].astype(str)
)

# TF-IDF vectorization
vectorizer = TfidfVectorizer(stop_words='english')
tfidf_matrix = vectorizer.fit_transform(df['combined'])
cosine_sim = cosine_similarity(tfidf_matrix, tfidf_matrix)

def recommend_similar_charities(charity_name, top_n=5):
    if charity_name not in df['name'].values:
        return []
    idx = df[df['name'] == charity_name].index[0]
    sim_scores = list(enumerate(cosine_sim[idx]))
    sim_scores = sorted(sim_scores, key=lambda x: x[1], reverse=True)[1:top_n+1]
    indices = [i[0] for i in sim_scores]
    return df.iloc[indices][['name', 'description', 'category']].to_dict(orient='records')

def recommend_by_interest(interest, top_n=5):
    interest_vec = vectorizer.transform([interest])
    sim_scores = cosine_similarity(interest_vec, tfidf_matrix).flatten()
    top_indices = sim_scores.argsort()[-top_n:][::-1]
    return df.iloc[top_indices][['name', 'description', 'category']].to_dict(orient='records')

# Command-line interface for Java
# Usage from Java: python charity_recommender.py mode "input text"
if __name__ == "__main__":
    mode = sys.argv[1]   # either 'similar' or 'interest'
    input_text = sys.argv[2]

    if mode == "similar":
        results = recommend_similar_charities(input_text)
    elif mode == "interest":
        results = recommend_by_interest(input_text)
    else:
        results = []

    for r in results:
        print(f"{r['name']}|{r['category']}|{r['description'][:120]}...")
