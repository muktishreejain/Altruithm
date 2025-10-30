# charity_recommender_model.py
"""
ML recommender backend.
- Uses columns: name, description, category, subcategory, motto, leader (if present).
- Exposes two functions: recommend_by_charity and recommend_by_interest.
- CLI usage prints JSON for easy integration with JavaFX subprocess.
"""

import argparse
import json
import sys
from typing import List, Dict

import numpy as np
import pandas as pd
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity

# -------------------- Load & Prepare --------------------
CSV_PATH = "charity.csv"

def load_data(path: str = CSV_PATH) -> pd.DataFrame:
    df = pd.read_csv(path, dtype=str).fillna("")
    # Make sure expected columns exist — use fallback names if necessary
    # Provided headings: ascore,category,description,ein,tot_exp,admin_exp_p,fund_eff,fund_exp_p,program_exp_p,
    # fscore,leader,leader_comp,leader_comp_p,motto,name,tot_rev,score,state,subcategory,size,program_exp,fund_exp,admin_exp
    # We will rely on: 'name', 'description', 'category', 'subcategory', 'motto', 'leader'
    expected = ['name', 'description', 'category', 'subcategory', 'motto', 'leader']
    for col in expected:
        if col not in df.columns:
            df[col] = ""
    # Create combined text field used for TF-IDF
    df['combined'] = (
        df['name'].astype(str) + " " +
        df['description'].astype(str) + " " +
        df['category'].astype(str) + " " +
        df['subcategory'].astype(str) + " " +
        df['motto'].astype(str) + " " +
        df['leader'].astype(str)
    )
    return df

df = load_data(CSV_PATH)

# -------------------- Vectorize --------------------
vectorizer = TfidfVectorizer(stop_words='english', max_features=20000)
tfidf_matrix = vectorizer.fit_transform(df['combined'])

# -------------------- Recommender Functions --------------------
def recommend_by_charity(charity_name: str, top_n: int = 5) -> List[Dict]:
    """
    Return list of top_n similar charities to charity_name.
    Each item: {name, description, category, subcategory, relevance}
    """
    # exact match search (case-insensitive)
    matches = df[df['name'].str.lower() == charity_name.strip().lower()]
    if matches.empty:
        # fallback: try substring match
        contains = df[df['name'].str.lower().str.contains(charity_name.strip().lower())]
        if not contains.empty:
            idx = contains.index[0]
        else:
            return []  # not found
    else:
        idx = matches.index[0]

    sim_scores = cosine_similarity(tfidf_matrix[idx], tfidf_matrix).flatten()
    # get descending indices, exclude itself
    sim_indices = sim_scores.argsort()[::-1]
    sim_indices = [i for i in sim_indices if i != idx][:top_n]

    results = []
    for i in sim_indices:
        results.append({
            "name": df.at[i, 'name'],
            "description": df.at[i, 'description'],
            "category": df.at[i, 'category'],
            "subcategory": df.at[i, 'subcategory'],
            "relevance": round(float(sim_scores[i]) * 100, 2)
        })
    return results


def recommend_by_interest(user_interest: str, top_n: int = 5) -> List[Dict]:
    """
    Return list of top_n charities matching the user interest text.
    """
    if not user_interest or user_interest.strip() == "":
        return []

    user_vec = vectorizer.transform([user_interest])
    sim_scores = cosine_similarity(user_vec, tfidf_matrix).flatten()
    top_indices = sim_scores.argsort()[::-1][:top_n]

    results = []
    for i in top_indices:
        results.append({
            "name": df.at[i, 'name'],
            "description": df.at[i, 'description'],
            "category": df.at[i, 'category'],
            "subcategory": df.at[i, 'subcategory'],
            "relevance": round(float(sim_scores[i]) * 100, 2)
        })
    return results

# -------------------- CLI / JSON Interface --------------------
def cli_main(argv=None):
    parser = argparse.ArgumentParser(description="Charity recommender CLI (outputs JSON).")
    sub = parser.add_subparsers(dest='cmd', required=True)

    p1 = sub.add_parser('charity', help='Recommend charities similar to a given charity name')
    p1.add_argument('--name', required=True, type=str, help='Charity name (string)')
    p1.add_argument('--top', required=False, type=int, default=5, help='Top N results')

    p2 = sub.add_parser('interest', help='Recommend charities based on interest text')
    p2.add_argument('--query', required=True, type=str, help='Interest text (e.g., "education, mental health")')
    p2.add_argument('--top', required=False, type=int, default=5, help='Top N results')

    args = parser.parse_args(argv)

    if args.cmd == 'charity':
        out = recommend_by_charity(args.name, args.top)
    elif args.cmd == 'interest':
        out = recommend_by_interest(args.query, args.top)
    else:
        out = []

    # Print JSON to stdout (so Java can parse)
    print(json.dumps({"results": out}, ensure_ascii=False))

if __name__ == "__main__":
    cli_main()
