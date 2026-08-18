from pydantic import BaseModel, ConfigDict, Field
from pydantic.alias_generators import to_camel
from typing import Optional

# --- Common Schemas ---

class BaseSchema(BaseModel):
    model_config = ConfigDict(
        alias_generator=to_camel,
        populate_by_name=True,
    )

class BookingContactInfo(BaseSchema):
    first_name: str = Field(alias="first_name")
    last_name: str = Field(alias="last_name")
    email: str
    country_code: str = Field(alias="country_code")
    phone_number: str = Field(alias="phone_number")

class UpdateStatusRequest(BaseModel):
    status: str

# --- Hotel Booking Schemas ---

class HotelBookingGuests(BaseSchema):
    adults: int = 1
    children: int = 0
    pets: bool = False

class HotelBookingPrice(BaseSchema):
    rate_per_night: float = Field(alias="rate_per_night")
    room_subtotal: float = Field(alias="room_subtotal")
    taxes: Optional[float] = 0.0
    fees: Optional[float] = 0.0
    total: float

class HotelBooking(BaseSchema):
    booking_id: str = Field(alias="booking_id")
    user_id: str = Field(None, alias="user_id")
    hotel_id: str = Field(alias="hotel_id")
    room_id: str = Field(alias="room_id")
    check_in: int = Field(alias="check_in")
    check_out: int = Field(alias="check_out")
    guests: HotelBookingGuests
    price: HotelBookingPrice
    status: str
    confirmation_code: str = Field(alias="confirmation_code")
    created_at: int = Field(alias="created_at")
    contact_info: Optional[BookingContactInfo] = None

# --- Tour Booking Schemas ---

class TourBookingParticipants(BaseSchema):
    adults: int = 1
    children: int = 0
    infants: int = 0

class TourBookingPrice(BaseSchema):
    rate_per_adult: float = Field(alias="rate_per_adult")
    rate_per_children: float = Field(alias="rate_per_children")
    rate_per_infant: float = Field(alias="rate_per_infant")
    subtotal: float
    taxes: float = 0.0
    fees: float = 0.0
    total: float

class TourBooking(BaseSchema):
    booking_id: str = Field(alias="booking_id")
    user_id: str = Field(alias="user_id")
    tour_id: str = Field(alias="tour_id")
    start_date: int = Field(alias="start_date")
    end_date: int = Field(alias="end_date")
    participants: TourBookingParticipants
    price: TourBookingPrice
    status: str
    confirmation_code: str = Field(alias="confirmation_code")
    created_at: int = Field(alias="created_at")
    contact_info: Optional[BookingContactInfo] = None
