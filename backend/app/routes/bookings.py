from fastapi import APIRouter, HTTPException
from app.database import supabase
from pydantic import BaseModel, ConfigDict, Field
from pydantic.alias_generators import to_camel
from typing import List, Optional

router = APIRouter(
    prefix="/bookings",
    tags=["Bookings"]
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
    userId: Optional[str] = Field(None, alias="userId")
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

class UpdateStatusRequest(BaseModel):
    status: str

@router.get("/")
def get_all_bookings():
    try:
        response = (
            supabase
            .table("hotels_booking")
            .select("*")
            .execute()
        )
        return response.data
    except Exception as e:
        print(f"ERROR fetching all bookings: {str(e)}")
        raise HTTPException(status_code=500, detail=str(e))

@router.put("/{booking_id}/status")
def update_booking_status(booking_id: str, request: UpdateStatusRequest):
    try:
        response = (
            supabase
            .table("hotels_booking")
            .update({"status": request.status})
            .eq("booking_id", booking_id)
            .execute()
        )
        if not response.data:
            raise HTTPException(status_code=404, detail="Booking not found")
        return {"status": "success", "data": response.data}
    except Exception as e:
        print(f"ERROR updating booking status: {str(e)}")
        raise HTTPException(status_code=500, detail=str(e))

@router.post("/")
def create_booking(booking: HotelBooking):
    try:
        # model_dump() uses field names by default, which are snake_case (e.g., user_id)
        # matches Supabase columns.
        booking_data = booking.model_dump()

        response = (
            supabase
            .table("hotels_booking")
            .insert(booking_data)
            .execute()
        )

        return {"status": "success", "data": response.data}
    except Exception as e:
        print(f"ERROR creating booking: {str(e)}")
        raise HTTPException(
            status_code=500,
            detail=f"Database error: {str(e)}"
        )
