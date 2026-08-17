from fastapi import APIRouter, HTTPException
from app.database import supabase
from typing import List, Optional
from pydantic import BaseModel, Field

router = APIRouter(
    prefix="/tours",
    tags=["Tours"]
)

class RatePerParticipant(BaseModel):
    adults: float = 0.0
    children: float = 0.0
    infants: float = 0.0

class TourModel(BaseModel):
    id: str
    title: str = ""
    description: str = ""
    location: str = ""
    image_url: List[str] = Field(default_factory=list, alias="image_url")
    duration: str = "PT0S"
    rates: RatePerParticipant = Field(default_factory=RatePerParticipant)
    rating: float = 0.0
    category: str = "ADVENTURE"
    number_of_reviews: int = Field(0, alias="number_of_reviews")
    included_services: List[str] = Field(default_factory=list, alias="included_services")
    latitude: float = 0.0
    longitude: float = 0.0

    class Config:
        populate_by_name = True

@router.get("/{tour_id}", response_model=TourModel)
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

        return response.data
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
        return response.data
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
