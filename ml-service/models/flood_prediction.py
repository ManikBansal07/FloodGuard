import numpy as np
from sklearn.ensemble import RandomForestRegressor
from sklearn.preprocessing import StandardScaler
import joblib
import os
from datetime import datetime, timedelta

class FloodPredictionModel:
    def __init__(self):
        self.model = None
        self.scaler = StandardScaler()
        self.model_path = os.path.join(os.path.dirname(__file__), 'flood_prediction_model.joblib')
        self.scaler_path = os.path.join(os.path.dirname(__file__), 'scaler.joblib')
        self.load_model()

    def load_model(self):
        """Load the trained model and scaler if they exist."""
        try:
            self.model = joblib.load(self.model_path)
            self.scaler = joblib.load(self.scaler_path)
        except:
            # If no model exists, create a new one
            self.model = RandomForestRegressor(n_estimators=100, random_state=42)
            # Train with dummy data for now
            self._train_dummy_model()

    def _train_dummy_model(self):
        """Train the model with dummy data for demonstration."""
        # Generate dummy training data
        np.random.seed(42)
        n_samples = 1000
        
        # Features: rainfall, temperature, humidity, wind_speed, elevation
        X = np.random.rand(n_samples, 5)
        # Target: flood risk level (0-1)
        y = np.random.rand(n_samples)
        
        # Scale features
        X_scaled = self.scaler.fit_transform(X)
        
        # Train model
        self.model.fit(X_scaled, y)
        
        # Save model and scaler
        joblib.dump(self.model, self.model_path)
        joblib.dump(self.scaler, self.scaler_path)

    def predict(self, features):
        """
        Make flood risk predictions.
        
        Args:
            features (dict): Dictionary containing:
                - latitude (float)
                - longitude (float)
                - weather_data (dict): Current weather conditions
                - historical_data (dict): Historical flood data
        
        Returns:
            dict: Prediction results including risk level and financial estimates
        """
        # Extract features from input
        weather_data = features.get('weather_data', {})
        historical_data = features.get('historical_data', {})
        
        # Prepare feature vector
        X = np.array([[
            weather_data.get('rainfall', 0),
            weather_data.get('temperature', 20),
            weather_data.get('humidity', 50),
            weather_data.get('wind_speed', 0),
            historical_data.get('elevation', 0)
        ]])
        
        # Scale features
        X_scaled = self.scaler.transform(X)
        
        # Make prediction
        risk_level = float(self.model.predict(X_scaled)[0])
        
        # Calculate financial estimates based on risk level
        property_damage = risk_level * 100000  # Example: max $100,000
        business_interruption = risk_level * 50000  # Example: max $50,000
        insurance_payout = risk_level * 80000  # Example: max $80,000
        
        # Determine risk category
        if risk_level < 0.2:
            risk_category = 'LOW'
        elif risk_level < 0.4:
            risk_category = 'MODERATE'
        elif risk_level < 0.6:
            risk_category = 'HIGH'
        elif risk_level < 0.8:
            risk_category = 'SEVERE'
        else:
            risk_category = 'EXTREME'
        
        return {
            'risk_level': risk_level,
            'risk_category': risk_category,
            'estimated_property_damage': property_damage,
            'estimated_business_interruption': business_interruption,
            'estimated_insurance_payout': insurance_payout,
            'prediction_time': datetime.now().isoformat(),
            'valid_until': (datetime.now() + timedelta(hours=24)).isoformat(),
            'model_version': '1.0.0'
        }

    def train(self, X, y):
        """
        Train the model with new data.
        
        Args:
            X (numpy.ndarray): Feature matrix
            y (numpy.ndarray): Target values
        """
        X_scaled = self.scaler.fit_transform(X)
        self.model.fit(X_scaled, y)
        
        # Save updated model and scaler
        joblib.dump(self.model, self.model_path)
        joblib.dump(self.scaler, self.scaler_path) 