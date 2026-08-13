from supabase import create_client
from dotenv import load_dotenv
import os

load_dotenv()

SUPABASE_URL = os.getenv("SUPABASE_URL")
SUPABASE_KEY = os.getenv("SUPABASE_SERVICE_ROLE_KEY") or os.getenv("SUPABASE_KEY")

if not SUPABASE_URL or not SUPABASE_KEY:
    raise ValueError("SUPABASE_URL and SUPABASE_SERVICE_ROLE_KEY (or SUPABASE_KEY) must be set in .env")

# Ensure URL is the base URL if it was accidentally copied with /rest/v1/
if SUPABASE_URL and "/rest/v1/" in SUPABASE_URL:
    SUPABASE_URL = SUPABASE_URL.split("/rest/v1/")[0]

supabase = create_client(SUPABASE_URL, SUPABASE_KEY)
