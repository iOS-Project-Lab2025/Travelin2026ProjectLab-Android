from fastapi import APIRouter, HTTPException
from app.database import supabase
from typing import List, Optional

router = APIRouter(
    prefix="/hotels",
    tags=["Hotels"]
)

@router.get("")
def get_hotels():
    response = (
        supabase
        .table("hotels")
        .select("*")
        .execute()
    )

    hotels = []

    for hotel in response.data:
        hotels.append({
            "id": hotel["id"],
            "name": hotel["name"],
            "address": hotel["address"],
            "star": hotel["star"],
            "userRating": hotel["user_rating"],
            "pricePerNight": hotel["price_per_night"],
            "image": hotel.get("image_list", []),
            "amenities": hotel.get("amenities", [])
        })

    return hotels

@router.get("/{id}/")
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

    hotel = hotel_response.data

    # Fetch associated rooms from the 'rooms' table
    rooms_response = (
        supabase
        .table("rooms")
        .select("*")
        .eq("hotel_id", id)
        .execute()
    )

    rooms = []
    for room in rooms_response.data:
        rooms.append({
            "id": str(room["id"]),
            "type": room["type"],
            "description": room.get("description", ""),
            "maxOccupancy": room["max_occupancy"],
            "bedType": room.get("bed_type", ""),
            "amenities": room.get("amenities", []),
            "pricePerNight": int(room["price_per_night"]),
            "images": room.get("images", []),
            "totalRooms": room["total_rooms"],
            "allowPets": room["allow_pets"],
        })

    return {
        "id": str(hotel["id"]),
        "name": hotel["name"],
        "address": hotel["address"],
        "star": hotel["star"],
        "userRating": hotel["user_rating"],
        "pricePerNight": hotel["price_per_night"],
        "image": hotel.get("image_list", []),
        "description": hotel.get("description", ""),
        "numberOfReviews": hotel.get("number_of_reviews", 0),
        "latitude": hotel.get("latitude", 0.0),
        "longitude": hotel.get("longitude", 0.0),
        "amenities": hotel.get("amenities", []),
        "rooms": rooms
    }

@router.get("/{id}/rooms/")
def get_hotel_rooms(id: str):
    response = (
        supabase
        .table("rooms")
        .select("*")
        .eq("hotel_id", id)
        .execute()
    )

    rooms = []
    for room in response.data:
        rooms.append({
            "id": str(room["id"]),
            "type": room["type"],
            "description": room.get("description", ""),
            "maxOccupancy": room["max_occupancy"],
            "bedType": room.get("bed_type", ""),
            "amenities": room.get("amenities", []),
            "pricePerNight": int(room["price_per_night"]),
            "images": room.get("images", []),
            "totalRooms": room["total_rooms"],
            "allowPets": room["allow_pets"],
        })
    return rooms
