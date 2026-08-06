# Travelin Backend

FastAPI backend for the Travelin Android application, integrated with Supabase.

## Project Structure

- `app/`: Main application package.
    - `routes/`: API endpoint definitions.
    - `services/`: Business logic and database operations.
    - `models/`: Pydantic models (schemas).
    - `main.py`: Entry point for the FastAPI application.
    - `database.py`: Supabase client configuration.
- `.env`: Environment variables (not tracked in Git).
- `requirements.txt`: Python dependencies.

## Setup

1. Create a virtual environment: `python -m venv venv`
2. Activate it: `source venv/bin/activate` (or `venv\Scripts\activate` on Windows)
3. Install dependencies: `pip install -r requirements.txt`
4. Run the server: `uvicorn app.main:app --reload`

## Tech Stack

- Python 3.12+
- FastAPI
- Uvicorn
- Supabase
- Pydantic

## Project Structure

```
backend/
│
├── app/
│   ├── routes/         # API endpoints
│   ├── services/       # Business logic
│   ├── models/         # Pydantic models
│   ├── database.py     # Supabase client
│   └── main.py         # FastAPI entry point
│
├── requirements.txt
├── .env.example
└── README.md
```

## Prerequisites

- Python 3.12 or newer
- Git

Verify your installation:

```bash
python --version
```

## Setup

1. Clone the repository.

2. Create a virtual environment.

```bash
python -m venv venv
```

3. Activate the virtual environment.

### Windows

```bash
venv\Scripts\activate
```

### macOS / Linux

```bash
source venv/bin/activate
```

4. Install the dependencies.

```bash
pip install -r requirements.txt
```

5. Create a `.env` file based on `.env.example`.

```env
SUPABASE_URL=...
SUPABASE_SERVICE_ROLE_KEY=...
```

6. Run the server.

```bash
uvicorn app.main:app --reload
```

The API will be available at:

```
http://127.0.0.1:8000
```

## API Documentation

FastAPI automatically generates documentation.

- Swagger UI: http://127.0.0.1:8000/docs
- ReDoc: http://127.0.0.1:8000/redoc

## Notes

- Never commit the `.env` file.
- The `venv/` directory should not be committed.
- Install new dependencies using:

```bash
pip install package_name
pip freeze > requirements.txt
```