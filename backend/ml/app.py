from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
from typing import List, Optional
import uvicorn
from flood_risk_model import FloodRiskModel, get_risk_level, get_risk_factors

app = FastAPI(title="FloodGuard ML Service")

# Configure CORS
app.add_middleware(
    CORSMiddleware,
    allow_origins=["http://localhost:3000"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Initialize the model
model = FloodRiskModel()
try:
    model.load_model()
except Exception as e:
    print(f"Warning: Could not load trained model: {e}")

class WeatherData(BaseModel):
    temperature: Optional[float]
    humidity: Optional[float]
    precipitation: Optional[float]
    wind_speed: Optional[float]

class FloodRiskRequest(BaseModel):
    weather: WeatherData

class FloodRiskResponse(BaseModel):
    risk_score: float
    risk_level: str
    factors: List[str]

@app.post("/predict", response_model=FloodRiskResponse)
async def predict_flood_risk(request: FloodRiskRequest):
    try:
        # Convert weather data to dict
        weather_dict = request.weather.dict()
        
        # Predict risk score
        risk_score = model.predict_risk(weather_dict)
        
        # Get risk level and factors
        risk_level = get_risk_level(risk_score)
        factors = get_risk_factors(weather_dict, risk_score)
        
        return FloodRiskResponse(
            risk_score=risk_score,
            risk_level=risk_level,
            factors=factors
        )
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

@app.get("/health")
async def health_check():
    return {"status": "healthy"}

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=5000) 