# FloodGuard

FloodGuard is a comprehensive flood prediction and management system that helps communities prepare for and respond to flood events.

## Features

- Real-time flood predictions using machine learning
- User authentication and authorization
- Location-based flood risk assessment
- Historical flood data analysis
- Automated alerts and notifications
- RESTful API for integration

## Tech Stack

- Backend: Spring Boot 3.2.3
- Database: PostgreSQL
- ML Service: Python with FastAPI
- Security: JWT Authentication
- Monitoring: Spring Boot Actuator, Prometheus
- Caching: Caffeine
- CI/CD: GitHub Actions

## Prerequisites

- Java 17 or higher
- Maven
- PostgreSQL
- Python 3.8+ (for ML service)

## Getting Started

1. Clone the repository:
```bash
git clone https://github.com/yourusername/floodguard.git
cd floodguard
```

2. Set up the database:
```bash
# Create PostgreSQL database
createdb floodguard
```

3. Configure the application:
- Copy `application.properties` to `application-local.properties`
- Update database credentials and other configurations

4. Build and run the backend:
```bash
cd backend
./mvnw spring-boot:run
```

5. Set up and run the ML service:
```bash
cd ml-service
python -m venv venv
source venv/bin/activate  # On Windows: venv\Scripts\activate
pip install -r requirements.txt
python main.py
```

## API Documentation

Once the application is running, you can access the API documentation at:
- Swagger UI: http://localhost:8080/api/swagger-ui.html
- OpenAPI Spec: http://localhost:8080/api/v3/api-docs

## Monitoring

The application exposes several monitoring endpoints:
- Health check: http://localhost:8080/api/actuator/health
- Metrics: http://localhost:8080/api/actuator/metrics
- Prometheus: http://localhost:8080/api/actuator/prometheus

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details. 