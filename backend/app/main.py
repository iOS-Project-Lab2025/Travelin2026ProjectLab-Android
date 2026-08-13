from fastapi import FastAPI
from app.routes.hotels import router as hotels_router
from app.routes.bookings import router as bookings_router
from app.database import supabase

app = FastAPI(
    title="Travelin Backend"
)

app.include_router(hotels_router)
app.include_router(bookings_router)


@app.get("/")
def root():
    return {
        "message": "Travelin Backend Running"
    }

@app.get("/health")
def health_check():
    try:
        supabase.table("hotels").select("count", count="exact").limit(1).execute()
        return {"status": "ok", "database": "connected"}
    except Exception as e:
        return {"status": "error", "message": str(e)}


if __name__ == "__main__":
    import uvicorn
    uvicorn.run("app.main:app", host="0.0.0.0", port=8000, reload=True)
