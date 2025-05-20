import numpy as np
import pandas as pd
from datetime import datetime, timedelta
import json
import os

def generate_weather_data(n_samples):
    """Generate dummy weather data."""
    np.random.seed(42)
    
    # Generate dates for the last year
    end_date = datetime.now()
    start_date = end_date - timedelta(days=365)
    dates = [start_date + timedelta(days=x) for x in range(n_samples)]
    
    # Generate weather features
    data = {
        'date': dates,
        'rainfall': np.random.normal(50, 20, n_samples).clip(0),  # mm
        'temperature': np.random.normal(20, 5, n_samples),  # °C
        'humidity': np.random.normal(60, 15, n_samples).clip(0, 100),  # %
        'wind_speed': np.random.normal(10, 5, n_samples).clip(0),  # km/h
        'elevation': np.random.normal(100, 50, n_samples).clip(0)  # meters
    }
    
    return pd.DataFrame(data)

def generate_historical_flood_data(n_samples):
    """Generate dummy historical flood data."""
    np.random.seed(42)
    
    # Generate dates for the last 5 years
    end_date = datetime.now()
    start_date = end_date - timedelta(days=5*365)
    dates = [start_date + timedelta(days=x) for x in range(n_samples)]
    
    # Generate flood events
    data = {
        'date': dates,
        'flood_occurred': np.random.binomial(1, 0.1, n_samples),  # 10% chance of flood
        'flood_depth': np.random.normal(0.5, 0.3, n_samples).clip(0),  # meters
        'damage_cost': np.random.normal(50000, 20000, n_samples).clip(0),  # USD
        'affected_area': np.random.normal(1000, 500, n_samples).clip(0)  # square meters
    }
    
    return pd.DataFrame(data)

def generate_training_data(n_samples=1000):
    """Generate complete training dataset."""
    # Generate weather and flood data
    weather_df = generate_weather_data(n_samples)
    flood_df = generate_historical_flood_data(n_samples)
    
    # Combine features
    X = weather_df[['rainfall', 'temperature', 'humidity', 'wind_speed', 'elevation']].values
    
    # Generate target variable (flood risk level)
    # Higher risk when:
    # - High rainfall
    # - High humidity
    # - Low elevation
    # - Historical flood occurrence
    risk_factors = (
        weather_df['rainfall'] / weather_df['rainfall'].max() * 0.4 +
        weather_df['humidity'] / 100 * 0.2 +
        (1 - weather_df['elevation'] / weather_df['elevation'].max()) * 0.2 +
        flood_df['flood_occurred'] * 0.2
    )
    
    y = risk_factors.values
    
    return X, y

def save_dummy_data():
    """Generate and save dummy data for training."""
    # Create data directory if it doesn't exist
    data_dir = os.path.join(os.path.dirname(__file__), '..', 'data')
    os.makedirs(data_dir, exist_ok=True)
    
    # Generate training data
    X, y = generate_training_data()
    
    # Save as numpy arrays
    np.save(os.path.join(data_dir, 'X_train.npy'), X)
    np.save(os.path.join(data_dir, 'y_train.npy'), y)
    
    # Generate and save weather data
    weather_df = generate_weather_data(1000)
    weather_df.to_csv(os.path.join(data_dir, 'weather_data.csv'), index=False)
    
    # Generate and save historical flood data
    flood_df = generate_historical_flood_data(1000)
    flood_df.to_csv(os.path.join(data_dir, 'historical_flood_data.csv'), index=False)
    
    print("Dummy data generated and saved successfully!")

if __name__ == '__main__':
    save_dummy_data() 