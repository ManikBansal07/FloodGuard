package com.floodguard.service;

import com.floodguard.model.Location;
import java.util.List;

public interface LocationService {
    Location createLocation(String token, Location location);
    List<Location> getAllLocations(String token, String search);
    Location getLocationById(String token, Long id);
    Location updateLocation(String token, Long id, Location location);
    void deleteLocation(String token, Long id);
    List<Location> getNearbyLocations(String token, Double latitude, Double longitude, Double radius);
} 