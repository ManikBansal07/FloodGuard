# FloodGuard

FloodGuard is a comprehensive flood monitoring and prediction system that helps communities stay safe and prepared for potential flood risks. The system uses advanced AI to predict and monitor flood conditions in real-time.

## Features

- Real-time flood monitoring
- AI-powered flood risk predictions
- Community-based flood reporting
- Interactive map visualization
- User authentication and role-based access
- Admin dashboard for report management

## Tech Stack

- **Frontend**: React + TypeScript + Tailwind CSS
- **Backend**: Spring Boot (Java)
- **ML Service**: Python (Flask)
- **Database**: PostgreSQL
- **Authentication**: JWT

## Prerequisites

- Java 17
- Node.js 16+
- Python 3.8+
- PostgreSQL
- Maven

## Setup Instructions

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/floodguard.git
   cd floodguard
   ```

2. Set up the backend:
   ```bash
   cd backend
   mvn clean install
   ```

3. Set up the frontend:
   ```bash
   cd frontend
   npm install
   ```

4. Set up the ML service:
   ```bash
   cd ml-service
   python -m venv venv
   source venv/bin/activate  # On Windows: venv\Scripts\activate
   pip install -r requirements.txt
   ```

5. Configure environment variables:
   - Copy `.env.example` to `.env` in each service directory
   - Update the configuration values as needed

6. Start the application:
   ```bash
   ./run.sh
   ```

## Accessing the Application

- Frontend: http://localhost:5173
- Backend: http://localhost:8080
- ML Service: http://localhost:5000

## Test Credentials

- Email: test@floodguard.com
- Password: Test@123

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details. 