package com.floodguard.controller;

import com.floodguard.model.WeatherData;
import com.floodguard.model.FloodRisk;
import com.floodguard.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @GetMapping("/current")
    public ResponseEntity<WeatherData> getCurrentWeather(
            @RequestParam Double latitude,
            @RequestParam Double longitude) {
        WeatherData weatherData = weatherService.getWeatherData(latitude, longitude);
        return ResponseEntity.ok(weatherData);
    }

    @GetMapping("/flood-risk")
    public ResponseEntity<FloodRisk> getFloodRisk(
            @RequestParam Double latitude,
            @RequestParam Double longitude) {
        FloodRisk floodRisk = weatherService.calculateFloodRisk(latitude, longitude);
        return ResponseEntity.ok(floodRisk);
    }
} 