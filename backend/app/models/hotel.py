from pydantic import BaseModel, Field
from typing import List, Optional

class RoomModel(BaseModel):
    id: str
    type: str
    description: str = ""
    maxOccupancy: int = Field(alias="max_occupancy")
    bedType: str = Field("", alias="bed_type")
    bedCount: int = Field(1, alias="bed_count")
    amenities: List[str] = Field(default_factory=list)
    pricePerNight: float = Field(alias="price_per_night")
    images: List[str] = Field(default_factory=list)
    totalRooms: int = Field(alias="total_rooms")
    allowPets: bool = Field(alias="allow_pets")

    class Config:
        populate_by_name = True

class HotelModel(BaseModel):
    id: str
    name: str
    address: str
    star: int
    userRating: float = Field(alias="user_rating")
    pricePerNight: float = Field(alias="price_per_night")
    image: List[str] = Field(default_factory=list, alias="image_list")
    amenities: List[str] = Field(default_factory=list)
    description: Optional[str] = ""
    numberOfReviews: int = Field(0, alias="number_of_reviews")
    latitude: float = 0.0
    longitude: float = 0.0
    rooms: List[RoomModel] = Field(default_factory=list)

    class Config:
        populate_by_name = True
