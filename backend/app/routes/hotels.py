from fastapi import APIRouter, HTTPException
from app.database import supabase
from app.models.hotel import HotelModel, RoomModel
from typing import List, Optional

router = APIRouter(
    prefix="/hotels",
    tags=["Hotels"]
)

@router.get("", response_model=List[HotelModel])
def get_hotels():
    response = (
        supabase
        .table("hotels")
        .select("*")
        .execute()
    )

    return [HotelModel(**hotel) for hotel in response.data]

@router.get("/{id}/", response_model=HotelModel)
def get_hotel_by_id(id: str):
    # Fetch hotel details
    hotel_response = (
        supabase
        .table("hotels")
        .select("*")
        .eq("id", id)
        .single()
        .execute()
    )

    if not hotel_response.data:
        raise HTTPException(status_code=404, detail="Hotel not found")

    hotel_data = hotel_response.data

    # Fetch associated rooms from the 'rooms' table
    rooms_response = (
        supabase
        .table("rooms")
        .select("*")
        .eq("hotel_id", id)
        .execute()
    )

    hotel_data["rooms"] = rooms_response.data

    return HotelModel(**hotel_data)

@router.get("/{id}/rooms/", response_model=List[RoomModel])
def get_hotel_rooms(id: str):
    response = (
        supabase
        .table("rooms")
        .select("*")
        .eq("hotel_id", id)
        .execute()
    )

    return [RoomModel(**room) for room in response.data]
