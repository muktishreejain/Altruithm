# charity_api.py
"""
FastAPI wrapper around charity_recommender_model.py functions.
Run with: uvicorn charity_api:app --host 0.0.0.0 --port 8000
"""

from fastapi import FastAPI, HTTPException, Query
from pydantic import BaseModel
from typing import List, Optional

# Import functions from the model file
from charity_recommender_model import recommend_by_charity, recommend_by_interest

app = FastAPI(title="Charity Recommender API", version="1.0")

class Recommendation(BaseModel):
    name: str
    description: str
    category: str
    subcategory: str
    relevance: float

@app.get("/recommend/charity", response_model=List[Recommendation])
def api_by_charity(name: str = Query(..., min_length=1), top: Optional[int] = 5):
    res = recommend_by_charity(name, top)
    if res is None:
        raise HTTPException(status_code=404, detail="Charity not found")
    return res

@app.get("/recommend/interest", response_model=List[Recommendation])
def api_by_interest(query: str = Query(..., min_length=1), top: Optional[int] = 5):
    res = recommend_by_interest(query, top)
    return res
