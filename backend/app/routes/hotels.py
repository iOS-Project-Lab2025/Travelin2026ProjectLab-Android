from fastapi import APIRouter, HTTPException
from app.database import supabase
from pydantic import BaseModel, ConfigDict, Field
from pydantic.alias_generators import to_camel
from typing import List, Optional

router = APIRouter(
    prefix="/hotels",
    tags=["Hotels"]
)

class BaseSchema(BaseModel):
    model_config = ConfigDict(
        alias_generator=to_camel,
        populate_by_name=True,
    )

class BookingGuests(BaseSchema):
    adults: int = 1
    children: int = 0
    pets: bool = False

class BookingPrice(BaseSchema):
    room_price_per_night: int = Field(alias="room_price_per_night")
    room_price: int = Field(alias="room_price")
    taxes: Optional[int] = 0
    fees: Optional[int] = 0
    total: int

class BookingContactInfo(BaseSchema):
    first_name: str = Field(alias="first_name")
    last_name: str = Field(alias="last_name")
    email: str
    country_code: str = Field(alias="country_code")
    phone_number: str = Field(alias="phone_number")

class HotelBooking(BaseSchema):
    booking_id: str = Field(alias="booking_id")
    hotel_id: str = Field(alias="hotel_id")
    room_id: str = Field(alias="room_id")
    check_in: int = Field(alias="check_in")
    check_out: int = Field(alias="check_out")
    guests: BookingGuests
    price: BookingPrice
    status: str
    confirmation_code: str = Field(alias="confirmation_code")
    created_at: int = Field(alias="created_at")
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

@router.post("/{hotel_id}/bookings/")
def create_booking(hotel_id: str, booking: HotelBooking):
    try:
        # Use model_dump() for Pydantic v2
        booking_data = booking.model_dump()

        # Insert into Supabase - Corrected table name
        response = (
            supabase
            .table("hotel_booking")
            .insert(booking_data)
            .execute()
        )

        return {"status": "success", "data": response.data}
    except Exception as e:
        # Print the error for Render logs and return a more helpful detail
        print(f"ERROR creating booking: {str(e)}")
        raise HTTPException(
            status_code=500,
            detail=f"Database error: {str(e)}. Make sure the 'bookings' table exists and has the correct columns."
        )
