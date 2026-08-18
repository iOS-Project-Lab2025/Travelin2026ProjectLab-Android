from fastapi import APIRouter, HTTPException
from app.database import supabase
from app.models.booking import (
    HotelBooking, TourBooking, UpdateStatusRequest
)

router = APIRouter(
    prefix="/bookings",
    tags=["Bookings"]
)

# --- Hotel Booking Routes ---

@router.get("/hotels")
def get_all_hotel_bookings():
    try:
        response = (
            supabase
            .table("hotels_booking")
            .select("*")
            .execute()
        )
        return response.data
    except Exception as e:
        print(f"ERROR fetching all hotel bookings: {str(e)}")
        raise HTTPException(status_code=500, detail=str(e))

@router.post("/hotels")
def create_hotel_booking(booking: HotelBooking):
    try:
        booking_data = booking.model_dump()
        response = (
            supabase
            .table("hotels_booking")
            .insert(booking_data)
            .execute()
        )
        return {"status": "success", "data": response.data}
    except Exception as e:
        print(f"ERROR creating hotel booking: {str(e)}")
        raise HTTPException(status_code=500, detail=f"Database error: {str(e)}")

@router.put("/hotels/{booking_id}/status")
def update_hotel_booking_status(booking_id: str, request: UpdateStatusRequest):
    try:
        response = (
            supabase
            .table("hotels_booking")
            .update({"status": request.status})
            .eq("booking_id", booking_id)
            .execute()
        )
        if not response.data:
            raise HTTPException(status_code=404, detail="Hotel booking not found")
        return {"status": "success", "data": response.data}
    except Exception as e:
        print(f"ERROR updating hotel booking status: {str(e)}")
        raise HTTPException(status_code=500, detail=str(e))

# --- Tour Booking Routes ---

@router.get("/tours")
def get_all_tour_bookings():
    try:
        response = (
            supabase
            .table("tours_booking")
            .select("*")
            .execute()
        )
        return response.data
    except Exception as e:
        print(f"ERROR fetching all tour bookings: {str(e)}")
        raise HTTPException(status_code=500, detail=str(e))

@router.post("/tours")
def create_tour_booking(booking: TourBooking):
    try:
        booking_data = booking.model_dump()
        response = (
            supabase
            .table("tours_booking")
            .insert(booking_data)
            .execute()
        )
        return {"status": "success", "data": response.data}
    except Exception as e:
        print(f"ERROR creating tour booking: {str(e)}")
        raise HTTPException(status_code=500, detail=f"Database error: {str(e)}")

@router.put("/tours/{booking_id}/status")
def update_tour_booking_status(booking_id: str, request: UpdateStatusRequest):
    try:
        response = (
            supabase
            .table("tours_booking")
            .update({"status": request.status})
            .eq("booking_id", booking_id)
            .execute()
        )
        if not response.data:
            raise HTTPException(status_code=404, detail="Tour booking not found")
        return {"status": "success", "data": response.data}
    except Exception as e:
        print(f"ERROR updating tour booking status: {str(e)}")
        raise HTTPException(status_code=500, detail=str(e))
