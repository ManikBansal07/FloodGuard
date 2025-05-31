package com.floodguard.service;

import com.floodguard.model.WeatherData;
import com.floodguard.model.FloodRisk;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

@Service
public class WeatherService {

    @Value("${weather.api.key}")
    private String weatherApiKey;

    @Value("${weather.api.url}")
    private String weatherApiUrl;

    @Value("${ml.service.url}")
    private String mlServiceUrl;

    private final RestTemplate restTemplate;
    private final Map<String, WeatherData> weatherCache;
    private final Map<String, FloodRisk> floodRiskCache;

    public WeatherService() {
        this.restTemplate = new RestTemplate();
        this.weatherCache = new HashMap<>();
        this.floodRiskCache = new HashMap<>();
    }

    @Cacheable(value = "weatherData", key = "#latitude + ',' + #longitude")
    public WeatherData getWeatherData(Double latitude, Double longitude) {
        String cacheKey = latitude + "," + longitude;
        
        // Check cache first
        if (weatherCache.containsKey(cacheKey)) {
            WeatherData cachedData = weatherCache.get(cacheKey);
            if (!isDataStale(cachedData.getTimestamp())) {
                return cachedData;
            }
        }

        // Fetch new data from weather API
        String url = String.format("%s?lat=%f&lon=%f&appid=%s&units=metric",
            weatherApiUrl, latitude, longitude, weatherApiKey);
        
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        
        WeatherData weatherData = new WeatherData();
        weatherData.setLatitude(latitude);
        weatherData.setLongitude(longitude);
        weatherData.setTemperature((Double) response.get("main.temp"));
        weatherData.setHumidity((Integer) response.get("main.humidity"));
        weatherData.setPrecipitation((Double) response.get("rain.1h"));
        weatherData.setWindSpeed((Double) response.get("wind.speed"));
        weatherData.setTimestamp(LocalDateTime.now());

        // Update cache
        weatherCache.put(cacheKey, weatherData);
        
        return weatherData;
    }

    @Cacheable(value = "floodRisk", key = "#latitude + ',' + #longitude")
    public FloodRisk calculateFloodRisk(Double latitude, Double longitude) {
        String cacheKey = latitude + "," + longitude;
        
        // Check cache first
        if (floodRiskCache.containsKey(cacheKey)) {
            FloodRisk cachedRisk = floodRiskCache.get(cacheKey);
            if (!isDataStale(cachedRisk.getTimestamp())) {
                return cachedRisk;
            }
        }

        // Get current weather data
        WeatherData weatherData = getWeatherData(latitude, longitude);
        
        try {
            // Call ML service for risk prediction
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> requestBody = new HashMap<>();
            Map<String, Object> weather = new HashMap<>();
            weather.put("temperature", weatherData.getTemperature());
            weather.put("humidity", weatherData.getHumidity());
            weather.put("precipitation", weatherData.getPrecipitation());
            weather.put("wind_speed", weatherData.getWindSpeed());
            requestBody.put("weather", weather);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(
                mlServiceUrl + "/predict",
                request,
                Map.class
            );

            Map<String, Object> prediction = response.getBody();
            
            FloodRisk floodRisk = new FloodRisk();
            floodRisk.setLatitude(latitude);
            floodRisk.setLongitude(longitude);
            floodRisk.setRiskScore((Double) prediction.get("risk_score"));
            floodRisk.setRiskLevel((String) prediction.get("risk_level"));
            floodRisk.setFactors((List<String>) prediction.get("factors"));
            floodRisk.setTimestamp(LocalDateTime.now());

            // Update cache
            floodRiskCache.put(cacheKey, floodRisk);
            
            return floodRisk;
        } catch (Exception e) {
            // Fallback to basic risk calculation if ML service is unavailable
            return calculateBasicFloodRisk(weatherData);
        }
    }

    private FloodRisk calculateBasicFloodRisk(WeatherData weatherData) {
        double riskScore = calculateRiskScore(weatherData);
        
        FloodRisk floodRisk = new FloodRisk();
        floodRisk.setLatitude(weatherData.getLatitude());
        floodRisk.setLongitude(weatherData.getLongitude());
        floodRisk.setRiskScore(riskScore);
        floodRisk.setRiskLevel(determineRiskLevel(riskScore));
        floodRisk.setFactors(getRiskFactors(weatherData));
        floodRisk.setTimestamp(LocalDateTime.now());
        
        return floodRisk;
    }

    private double calculateRiskScore(WeatherData weatherData) {
        double score = 0.0;
        
        // Precipitation factor (0-40 points)
        if (weatherData.getPrecipitation() != null) {
            score += Math.min(weatherData.getPrecipitation() * 4, 40);
        }
        
        // Humidity factor (0-20 points)
        if (weatherData.getHumidity() != null) {
            score += (weatherData.getHumidity() / 100.0) * 20;
        }
        
        // Wind speed factor (0-20 points)
        if (weatherData.getWindSpeed() != null) {
            score += Math.min(weatherData.getWindSpeed() / 2, 20);
        }
        
        // Temperature factor (0-20 points)
        if (weatherData.getTemperature() != null) {
            // Higher risk for temperatures near freezing (0°C)
            double tempFactor = Math.abs(weatherData.getTemperature());
            score += Math.max(0, 20 - (tempFactor * 2));
        }
        
        return Math.min(score, 100);
    }

    private String determineRiskLevel(double riskScore) {
        if (riskScore >= 80) return "EXTREME";
        if (riskScore >= 60) return "HIGH";
        if (riskScore >= 40) return "MEDIUM";
        if (riskScore >= 20) return "LOW";
        return "MINIMAL";
    }

    private List<String> getRiskFactors(WeatherData weatherData) {
        List<String> factors = new ArrayList<>();
        
        if (weatherData.getPrecipitation() != null && weatherData.getPrecipitation() > 10) {
            factors.add("Heavy precipitation");
        }
        
        if (weatherData.getHumidity() != null && weatherData.getHumidity() > 80) {
            factors.add("High humidity");
        }
        
        if (weatherData.getWindSpeed() != null && weatherData.getWindSpeed() > 20) {
            factors.add("Strong winds");
        }
        
        if (weatherData.getTemperature() != null && Math.abs(weatherData.getTemperature()) < 5) {
            factors.add("Near-freezing temperatures");
        }
        
        return factors;
    }

    private boolean isDataStale(LocalDateTime timestamp) {
        return LocalDateTime.now().minusMinutes(15).isAfter(timestamp);
    }

    @Scheduled(fixedRate = 900000) // Every 15 minutes
    @CacheEvict(value = {"weatherData", "floodRisk"}, allEntries = true)
    public void clearStaleCache() {
        weatherCache.clear();
        floodRiskCache.clear();
    }
} 