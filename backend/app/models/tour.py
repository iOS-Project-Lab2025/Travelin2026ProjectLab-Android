from pydantic import BaseModel, Field
from typing import List

class RatePerParticipant(BaseModel):
    adults: float = 0.0
    children: float = 0.0
    infants: float = 0.0

class TourModel(BaseModel):
    id: str
    title: str = ""
    description: str = ""
    location: str = ""
    image_url: List[str] = Field(default_factory=list, alias="image_url")
    duration: str = "PT0S"
    rates: RatePerParticipant = Field(default_factory=RatePerParticipant)
    rating: float = 0.0
    category: str = "ADVENTURE"
    number_of_reviews: int = Field(0, alias="number_of_reviews")
    included_services: List[str] = Field(default_factory=list, alias="included_services")
    latitude: float = 0.0
    longitude: float = 0.0

    class Config:
        populate_by_name = True
