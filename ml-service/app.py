from flask import Flask, request, jsonify
from flask_cors import CORS
import numpy as np
from datetime import datetime, timedelta
import joblib
import os
from dotenv import load_dotenv

# Load environment variables
load_dotenv()

app = Flask(__name__)
CORS(app)

# Placeholder for ML model
class FloodPredictionModel:
    def __init__(self):
        self.model = None
        # In production, load the actual trained model
        # self.model = joblib.load('models/flood_prediction_model.joblib')
    
    def predict(self, features):
        # Placeholder prediction logic
        # In production, use the actual model for prediction
        risk_level = np.random.random()  # Random value between 0 and 1
        return {
            'risk_level': float(risk_level),
            'risk_category': self._get_risk_category(risk_level),
            'estimated_property_damage': float(risk_level * 100000),
            'estimated_business_interruption': float(risk_level * 50000),
            'estimated_insurance_payout': float(risk_level * 80000),
            'prediction_time': datetime.now().isoformat(),
            'valid_until': (datetime.now() + timedelta(hours=24)).isoformat(),
            'model_version': '1.0.0'
        }
    
    def _get_risk_category(self, risk_level):
        if risk_level < 0.2:
            return 'LOW'
        elif risk_level < 0.4:
            return 'MODERATE'
        elif risk_level < 0.6:
            return 'HIGH'
        elif risk_level < 0.8:
            return 'SEVERE'
        else:
            return 'EXTREME'

model = FloodPredictionModel()

@app.route('/api/predict', methods=['POST'])
def predict():
    try:
        data = request.json
        features = {
            'latitude': data.get('latitude'),
            'longitude': data.get('longitude'),
            'weather_data': data.get('weather_data', {}),
            'historical_data': data.get('historical_data', {})
        }
        
        prediction = model.predict(features)
        return jsonify(prediction)
    
    except Exception as e:
        return jsonify({'error': str(e)}), 500

@app.route('/api/health', methods=['GET'])
def health_check():
    return jsonify({'status': 'healthy', 'timestamp': datetime.now().isoformat()})

if __name__ == '__main__':
    port = int(os.getenv('PORT', 5000))
    app.run(host='0.0.0.0', port=port, debug=True) 