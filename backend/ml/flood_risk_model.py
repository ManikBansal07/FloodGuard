import numpy as np
import pandas as pd
from sklearn.ensemble import RandomForestRegressor
from sklearn.preprocessing import StandardScaler
import joblib
import os

class FloodRiskModel:
    def __init__(self):
        self.model = RandomForestRegressor(
            n_estimators=100,
            max_depth=10,
            random_state=42
        )
        self.scaler = StandardScaler()
        self.is_trained = False

    def preprocess_features(self, weather_data):
        """Convert weather data into model features"""
        features = np.array([
            weather_data['temperature'],
            weather_data['humidity'],
            weather_data['precipitation'],
            weather_data['wind_speed']
        ]).reshape(1, -1)
        
        if self.is_trained:
            features = self.scaler.transform(features)
        
        return features

    def predict_risk(self, weather_data):
        """Predict flood risk score based on weather data"""
        if not self.is_trained:
            return self._calculate_basic_risk(weather_data)
        
        features = self.preprocess_features(weather_data)
        risk_score = self.model.predict(features)[0]
        return min(max(risk_score, 0), 100)  # Ensure score is between 0 and 100

    def _calculate_basic_risk(self, weather_data):
        """Calculate basic risk score when model is not trained"""
        risk_score = 0
        
        # Precipitation factor (0-40 points)
        if weather_data['precipitation'] is not None:
            risk_score += min(weather_data['precipitation'] * 4, 40)
        
        # Humidity factor (0-20 points)
        if weather_data['humidity'] is not None:
            risk_score += (weather_data['humidity'] / 100.0) * 20
        
        # Wind speed factor (0-20 points)
        if weather_data['wind_speed'] is not None:
            risk_score += min(weather_data['wind_speed'] / 2, 20)
        
        # Temperature factor (0-20 points)
        if weather_data['temperature'] is not None:
            # Higher risk for temperatures near freezing (0°C)
            temp_factor = abs(weather_data['temperature'])
            risk_score += max(0, 20 - (temp_factor * 2))
        
        return min(risk_score, 100)

    def train(self, historical_data):
        """Train the model on historical data"""
        if not historical_data:
            return
        
        # Prepare training data
        X = []
        y = []
        
        for data_point in historical_data:
            features = self.preprocess_features(data_point['weather'])
            X.append(features[0])
            y.append(data_point['actual_risk'])
        
        X = np.array(X)
        y = np.array(y)
        
        # Scale features
        X = self.scaler.fit_transform(X)
        
        # Train model
        self.model.fit(X, y)
        self.is_trained = True

    def save_model(self, model_path='flood_risk_model.joblib'):
        """Save the trained model and scaler"""
        if not self.is_trained:
            return
        
        model_data = {
            'model': self.model,
            'scaler': self.scaler
        }
        joblib.dump(model_data, model_path)

    def load_model(self, model_path='flood_risk_model.joblib'):
        """Load a trained model and scaler"""
        if not os.path.exists(model_path):
            return
        
        model_data = joblib.load(model_path)
        self.model = model_data['model']
        self.scaler = model_data['scaler']
        self.is_trained = True

def get_risk_level(risk_score):
    """Convert risk score to risk level"""
    if risk_score >= 80:
        return "EXTREME"
    elif risk_score >= 60:
        return "HIGH"
    elif risk_score >= 40:
        return "MEDIUM"
    elif risk_score >= 20:
        return "LOW"
    else:
        return "MINIMAL"

def get_risk_factors(weather_data, risk_score):
    """Determine risk factors based on weather data and risk score"""
    factors = []
    
    if weather_data['precipitation'] is not None and weather_data['precipitation'] > 10:
        factors.append("Heavy precipitation")
    
    if weather_data['humidity'] is not None and weather_data['humidity'] > 80:
        factors.append("High humidity")
    
    if weather_data['wind_speed'] is not None and weather_data['wind_speed'] > 20:
        factors.append("Strong winds")
    
    if weather_data['temperature'] is not None and abs(weather_data['temperature']) < 5:
        factors.append("Near-freezing temperatures")
    
    if risk_score >= 80:
        factors.append("Extreme flood risk conditions")
    elif risk_score >= 60:
        factors.append("High flood risk conditions")
    
    return factors 