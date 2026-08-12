from fastapi import APIRouter, HTTPException
from app.database import supabase
from pydantic import BaseModel
from typing import List, Optional

router = APIRouter(
    prefix="/hotels",
    tags=["Hotels"]
)

class BookingGuests(BaseModel):
    adults: int
    children: int
    pets: bool

class BookingPrice(BaseModel):
    room_price_per_night: int
    room_price: int
    taxes: Optional[int] = 0
    fees: Optional[int] = 0
    total: int

class BookingContactInfo(BaseModel):
    first_name: str
    last_name: str
    email: str
    country_code: str
    phone_number: str

class HotelBooking(BaseModel):
    booking_id: str
    hotel_id: str
    room_id: str
    check_in: int
    check_out: int
    guests: BookingGuests
    price: BookingPrice
    status: str
    confirmation_code: str
    created_at: int
    contact_info: Optional[BookingContactInfo] = None

@router.get("")
def get_hotels():
    # ... existing code ...
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
            "image": hotel.get("image_list", [])
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
            "availableRooms": room["available_rooms"],
            "isAvailable": room["is_available"]
        })

    return {
        "id": str(hotel["id"]),
        "name": hotel["name"],
        "address": hotel["address"],
        "star": hotel["star"],
        "userRating": hotel["user_rating"],
        "pricePerNight": hotel["price_per_night"],
        "image": hotel.get("image_list", []),
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
            "availableRooms": room["available_rooms"],
            "isAvailable": room["is_available"]
        })
    return rooms

@router.post("/{hotel_id}/bookings")
def create_booking(hotel_id: str, booking: HotelBooking):
    # Use model_dump() for Pydantic v2
    booking_data = booking.model_dump()

    response = (
        supabase
        .table("bookings")
        .insert(booking_data)
        .execute()
    )

    return {"status": "success", "data": response.data}
