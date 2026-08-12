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
    taxes: int
    fees: int
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

@router.get("/{id}")
def get_hotel_by_id(id: str):
    response = (
        supabase
        .table("hotels")
        .select("*")
        .eq("id", id)
        .single()
        .execute()
    )

    if not response.data:
        raise HTTPException(status_code=404, detail="Hotel not found")

    hotel = response.data
    return {
        "id": hotel["id"],
        "name": hotel["name"],
        "address": hotel["address"],
        "star": hotel["star"],
        "userRating": hotel["user_rating"],
        "pricePerNight": hotel["price_per_night"],
        "image": hotel.get("image_list", [])
    }

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
