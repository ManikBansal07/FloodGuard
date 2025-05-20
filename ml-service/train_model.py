import os
import numpy as np
from models.flood_prediction import FloodPredictionModel

def train_model():
    """Train the flood prediction model using dummy data."""
    # Load training data
    data_dir = os.path.join(os.path.dirname(__file__), 'data')
    X = np.load(os.path.join(data_dir, 'X_train.npy'))
    y = np.load(os.path.join(data_dir, 'y_train.npy'))
    
    # Initialize and train model
    model = FloodPredictionModel()
    model.train(X, y)
    
    print("Model trained successfully!")

if __name__ == '__main__':
    train_model() 