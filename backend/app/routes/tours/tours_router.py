from fastapi import APIRouter, HTTPException
from app.database import supabase
from typing import List, Any

router = APIRouter(
    prefix="/tours",
    tags=["Tours"]
)

@router.get("/{tour_id}")
def get_tour_by_id(tour_id: str):
    try:
        response = (
            supabase
            .table("tours")
            .select("*")
            .eq("id", tour_id)
            .single()
            .execute()
        )

        if not response.data:
            raise HTTPException(status_code=404, detail="Tour not found")

        tour = response.data
        return {
            "id": tour["id"],
            "title": tour.get("title", ""),
            "description": tour.get("description", ""),
            "location": tour.get("location", ""),
            "image_url": tour.get("image_url", []),
            "duration": tour.get("duration", "PT0S"),
            "price": tour.get("price", 0.0),
            "rating": tour.get("rating", 0.0),
            "category": tour.get("category", "ADVENTURE"),
            "number_of_reviews": tour.get("number_of_reviews", 0),
            "included_services": tour.get("included_services", []),
            "latitude": tour.get("latitude", 0.0),
            "longitude": tour.get("longitude", 0.0)
        }
    except Exception as e:
        # Check if it's a 406 (Not Acceptable) or 404 from PostgREST
        if "details" in str(e) or "PGRST116" in str(e):
             raise HTTPException(status_code=404, detail="Tour not found")
        raise HTTPException(status_code=500, detail=str(e))

@router.get("")
def get_tours():
    try:
        response = (
            supabase
            .table("tours")
            .select("*")
            .execute()
        )
        
        tours = []
        for tour in response.data:
            tours.append({
                "id": tour["id"],
                "title": tour.get("title", ""),
                "description": tour.get("description", ""),
                "location": tour.get("location", ""),
                "image_url": tour.get("image_url", []),
                "duration": tour.get("duration", "PT0S"),
                "price": tour.get("price", 0.0),
                "rating": tour.get("rating", 0.0),
                "category": tour.get("category", "ADVENTURE"),
                "number_of_reviews": tour.get("number_of_reviews", 0),
                "included_services": tour.get("included_services", []),
                "latitude": tour.get("latitude", 0.0),
                "longitude": tour.get("longitude", 0.0)
            })
        return tours
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
