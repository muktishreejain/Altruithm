from flask import Flask, request, jsonify
import sys
import io
sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding='utf-8')
import os
import pandas as pd
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity



app = Flask(__name__)



BASE_DIR = os.path.dirname(os.path.abspath(__file__))

DATA_PATH = os.path.join(BASE_DIR, 'CharityData_NonProfit_cleaned_FINAL.csv')



df = pd.read_csv(DATA_PATH)

df['combined'] = df['name'].astype(str) + " " + df['description'].astype(str) + " " + df['category'].astype(str)



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



@app.route('/recommend_similar')

def api_similar():

    charity_name = request.args.get('charity_name', '')

    top_n = int(request.args.get('top_n', 5))

    result = recommend_similar_charities(charity_name, top_n)

    return jsonify(result)



@app.route('/recommend_interest')

def api_interest():

    interest = request.args.get('interest', '')

    top_n = int(request.args.get('top_n', 5))

    result = recommend_by_interest(interest, top_n)

    return jsonify(result)



if __name__ == '__main__':

    app.run(debug=True)
