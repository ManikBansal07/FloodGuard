from flask import Flask, request, jsonify
from flask_cors import CORS
import numpy as np
from models.flood_predictor import FloodPredictor
from models.financial_risk import FinancialRiskCalculator
import logging

app = Flask(__name__)
CORS(app)

# Configure logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

# Initialize models
flood_predictor = FloodPredictor()
financial_risk = FinancialRiskCalculator()

@app.route('/health', methods=['GET'])
def health_check():
    return jsonify({"status": "healthy"}), 200

@app.route('/predict', methods=['POST'])
def predict_flood():
    try:
        data = request.json
        location = data.get('location')
        weather_data = data.get('weather_data')
        historical_data = data.get('historical_data')
        
        prediction = flood_predictor.predict(location, weather_data, historical_data)
        return jsonify(prediction), 200
    except Exception as e:
        logger.error(f"Error in flood prediction: {str(e)}")
        return jsonify({"error": str(e)}), 500

@app.route('/financial-risk', methods=['POST'])
def calculate_financial_risk():
    try:
        data = request.json
        location = data.get('location')
        property_value = data.get('property_value')
        flood_severity = data.get('flood_severity')
        
        risk_assessment = financial_risk.calculate(
            location=location,
            property_value=property_value,
            flood_severity=flood_severity
        )
        return jsonify(risk_assessment), 200
    except Exception as e:
        logger.error(f"Error in financial risk calculation: {str(e)}")
        return jsonify({"error": str(e)}), 500

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=True) 