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

        # The supabase-py client raises an error for 404/Empty usually,
        # but let's check response.data for safety.
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
