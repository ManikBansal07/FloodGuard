import numpy as np
from sklearn.ensemble import RandomForestRegressor
from sklearn.preprocessing import StandardScaler
import joblib
import os
import logging

logger = logging.getLogger(__name__)

class FloodPredictor:
    def __init__(self):
        self.model = None
        self.scaler = StandardScaler()
        self.model_path = os.path.join(os.path.dirname(__file__), 'models', 'flood_model.joblib')
        self.scaler_path = os.path.join(os.path.dirname(__file__), 'models', 'scaler.joblib')
        self._load_or_initialize_model()

    def _load_or_initialize_model(self):
        try:
            if os.path.exists(self.model_path) and os.path.exists(self.scaler_path):
                self.model = joblib.load(self.model_path)
                self.scaler = joblib.load(self.scaler_path)
                logger.info("Loaded existing model and scaler")
            else:
                self.model = RandomForestRegressor(
                    n_estimators=100,
                    max_depth=10,
                    random_state=42
                )
                logger.info("Initialized new model")
        except Exception as e:
            logger.error(f"Error loading model: {str(e)}")
            self.model = RandomForestRegressor(
                n_estimators=100,
                max_depth=10,
                random_state=42
            )

    def _prepare_features(self, location, weather_data, historical_data):
        # Combine weather and historical data into feature vector
        features = []
        
        # Weather features
        if weather_data:
            features.extend([
                weather_data.get('precipitation', 0),
                weather_data.get('temperature', 0),
                weather_data.get('humidity', 0),
                weather_data.get('wind_speed', 0)
            ])
        
        # Historical features
        if historical_data:
            features.extend([
                historical_data.get('avg_precipitation', 0),
                historical_data.get('flood_frequency', 0),
                historical_data.get('elevation', 0),
                historical_data.get('drainage_capacity', 0)
            ])
        
        # Location features
        if location:
            features.extend([
                location.get('latitude', 0),
                location.get('longitude', 0),
                location.get('elevation', 0)
            ])
        
        return np.array(features).reshape(1, -1)

    def predict(self, location, weather_data, historical_data):
        try:
            # Prepare features
            features = self._prepare_features(location, weather_data, historical_data)
            
            # Scale features
            scaled_features = self.scaler.fit_transform(features)
            
            # Make prediction
            prediction = self.model.predict(scaled_features)[0]
            
            # Calculate confidence score (example implementation)
            confidence = self._calculate_confidence(features)
            
            return {
                'flood_risk': float(prediction),
                'confidence': float(confidence),
                'timestamp': np.datetime64('now').astype(str)
            }
        except Exception as e:
            logger.error(f"Error in prediction: {str(e)}")
            raise

    def _calculate_confidence(self, features):
        # Simple confidence calculation based on feature completeness
        # This is a placeholder - implement more sophisticated confidence calculation
        return 0.8

    def train(self, X, y):
        try:
            # Scale features
            X_scaled = self.scaler.fit_transform(X)
            
            # Train model
            self.model.fit(X_scaled, y)
            
            # Save model and scaler
            os.makedirs(os.path.dirname(self.model_path), exist_ok=True)
            joblib.dump(self.model, self.model_path)
            joblib.dump(self.scaler, self.scaler_path)
            
            logger.info("Model trained and saved successfully")
        except Exception as e:
            logger.error(f"Error training model: {str(e)}")
            raise 