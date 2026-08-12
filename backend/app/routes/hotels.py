from fastapi import APIRouter
from app.database import supabase

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
            "pricePerNight": hotel["price_per_night"]
        })

    return hotels