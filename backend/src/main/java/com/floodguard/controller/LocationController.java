package com.floodguard.controller;

import com.floodguard.model.Location;
import com.floodguard.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
@CrossOrigin(origins = "*")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @PostMapping
    public ResponseEntity<?> createLocation(
            @RequestHeader("Authorization") String token,
            @RequestBody Location location) {
        try {
            Location createdLocation = locationService.createLocation(token, location);
            return ResponseEntity.ok(createdLocation);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllLocations(
            @RequestHeader("Authorization") String token,
            @RequestParam(required = false) String search) {
        try {
            List<Location> locations = locationService.getAllLocations(token, search);
            return ResponseEntity.ok(locations);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getLocationById(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id) {
        try {
            Location location = locationService.getLocationById(token, id);
            return ResponseEntity.ok(location);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateLocation(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id,
            @RequestBody Location location) {
        try {
            Location updatedLocation = locationService.updateLocation(token, id, location);
            return ResponseEntity.ok(updatedLocation);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLocation(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id) {
        try {
            locationService.deleteLocation(token, id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/nearby")
    public ResponseEntity<?> getNearbyLocations(
            @RequestHeader("Authorization") String token,
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam Double radius) {
        try {
            List<Location> locations = locationService.getNearbyLocations(token, latitude, longitude, radius);
            return ResponseEntity.ok(locations);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
} 