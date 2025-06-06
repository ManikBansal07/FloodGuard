# FloodGuard - Intelligent Flood Monitoring and Prediction System

FloodGuard is a comprehensive flood monitoring and prediction system that combines real-time data analysis, machine learning, and community reporting to provide accurate flood risk assessments and timely alerts..

## Features

- Real-time flood prediction using weather data and historical patterns
- Interactive risk maps with color-coded zones
- Location-based alert system via email and SMS
- Financial risk assessment for properties and businesses
- Community reporting system for flood observations
- Admin dashboard for report verification

## Tech Stack

- **Backend:** Java Spring Boot
- **Frontend:** React.js with Tailwind CSS
- **Database:** PostgreSQL
- **Machine Learning:** Python (scikit-learn/TensorFlow)
- **Mapping:** Leaflet.js
- **Notifications:** Twilio/Firebase

## Project Structure

```
FloodGuard/
├── backend/                 # Spring Boot application
├── frontend/               # React application
├── ml-models/             # Python ML models
└── docs/                  # Documentation
```

## Setup Instructions

### Prerequisites

- Java 17 or higher
- Node.js 18 or higher
- Python 3.8 or higher
- PostgreSQL 14 or higher
- Maven
- npm

### Backend Setup

1. Navigate to the backend directory:
   ```bash
   cd backend
   ```

2. Install dependencies:
   ```bash
   mvn install
   ```

3. Configure PostgreSQL connection in `application.properties`

4. Run the application:
   ```bash
   mvn spring-boot:run
   ```

### Frontend Setup

1. Navigate to the frontend directory:
   ```bash
   cd frontend
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Start the development server:
   ```bash
   npm start
   ```

### ML Models Setup

1. Navigate to the ml-models directory:
   ```bash
   cd ml-models
   ```

2. Create a virtual environment:
   ```bash
   python -m venv venv
   source venv/bin/activate  # On Windows: venv\Scripts\activate
   ```

3. Install dependencies:
   ```bash
   pip install -r requirements.txt
   ```

4. Run the model training script:
   ```bash
   python train_models.py
   ```

## API Documentation

The API documentation is available at `/swagger-ui.html` when running the backend server.

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details. 
