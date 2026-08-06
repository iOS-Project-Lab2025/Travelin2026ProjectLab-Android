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
        .select("""
            *,
            hotel_images(
                image_url
            )
        """)
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
            "image": [
                img["image_url"]
                for img in hotel.get("hotel_images", [])
            ]
        })

    return hotels

    return response.data