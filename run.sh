#!/bin/bash

# Start PostgreSQL
pg_ctl -D /opt/homebrew/var/postgresql@14 start

# Create database if it doesn't exist
createdb floodguard 2>/dev/null || true

# Start Spring Boot backend
cd backend
mvn spring-boot:run &
BACKEND_PID=$!

# Start Python ML service
cd ../ml-service
python -m venv venv
source venv/bin/activate
pip install -r requirements.txt
python app.py &
ML_PID=$!

# Start React frontend
cd ../frontend
npm install
npm run dev &
FRONTEND_PID=$!

# Function to cleanup processes on exit
cleanup() {
    echo "Stopping services..."
    kill $BACKEND_PID
    kill $ML_PID
    kill $FRONTEND_PID
    pg_ctl -D /opt/homebrew/var/postgresql@14 stop
    exit
}

# Register cleanup function
trap cleanup SIGINT SIGTERM

# Wait for all background processes
wait 