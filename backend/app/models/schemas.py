from pydantic import BaseModel
from typing import Optional

# Since the tours table already exists and we don't have its exact schema,
# we keep the schema file simple or focused on common elements if needed.
# For now, we will let FastAPI return the raw dictionaries from Supabase
# to ensure compatibility with the existing table without strict Pydantic enforcement
# if the schema differs from our previous assumptions.

class TourSchema(BaseModel):
    # This can be used for documentation or basic validation if the schema is known later.
    pass
