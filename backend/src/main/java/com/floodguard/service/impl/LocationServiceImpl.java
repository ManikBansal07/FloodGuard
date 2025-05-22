package com.floodguard.service.impl;

import com.floodguard.model.Location;
import com.floodguard.model.User;
import com.floodguard.repository.LocationRepository;
import com.floodguard.repository.UserRepository;
import com.floodguard.security.JwtTokenProvider;
import com.floodguard.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LocationServiceImpl implements LocationService {

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public Location createLocation(String token, Location location) {
        String email = jwtTokenProvider.getUsernameFromToken(token);
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

        // Validate coordinates
        if (location.getLatitude() == null || location.getLongitude() == null) {
            throw new RuntimeException("Location coordinates are required");
        }

        // Check if location already exists
        if (locationRepository.existsByLatitudeAndLongitude(
                location.getLatitude(), location.getLongitude())) {
            throw new RuntimeException("Location already exists");
        }

        return locationRepository.save(location);
    }

    @Override
    public List<Location> getAllLocations(String token, String search) {
        String email = jwtTokenProvider.getUsernameFromToken(token);
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

        if (search != null && !search.isEmpty()) {
            return locationRepository.findByNameContainingIgnoreCaseOrAddressContainingIgnoreCase(search, search);
        }
        return locationRepository.findAll();
    }

    @Override
    public Location getLocationById(String token, Long id) {
        String email = jwtTokenProvider.getUsernameFromToken(token);
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

        return locationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Location not found"));
    }

    @Override
    public Location updateLocation(String token, Long id, Location updatedLocation) {
        String email = jwtTokenProvider.getUsernameFromToken(token);
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

        Location existingLocation = locationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Location not found"));

        // Only allow updates if user is an admin
        if (!user.getRole().equals("ADMIN")) {
            throw new RuntimeException("Not authorized to update location");
        }

        existingLocation.setName(updatedLocation.getName());
        existingLocation.setAddress(updatedLocation.getAddress());
        existingLocation.setLatitude(updatedLocation.getLatitude());
        existingLocation.setLongitude(updatedLocation.getLongitude());
        existingLocation.setDescription(updatedLocation.getDescription());

        return locationRepository.save(existingLocation);
    }

    @Override
    public void deleteLocation(String token, Long id) {
        String email = jwtTokenProvider.getUsernameFromToken(token);
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

        Location location = locationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Location not found"));

        // Only allow deletion if user is an admin
        if (!user.getRole().equals("ADMIN")) {
            throw new RuntimeException("Not authorized to delete location");
        }

        locationRepository.delete(location);
    }

    @Override
    public List<Location> getNearbyLocations(String token, Double latitude, Double longitude, Double radius) {
        String email = jwtTokenProvider.getUsernameFromToken(token);
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

        // Get all locations and filter by distance
        return locationRepository.findAll().stream()
            .filter(location -> calculateDistance(
                latitude, longitude,
                location.getLatitude(), location.getLongitude()) <= radius)
            .collect(Collectors.toList());
    }

    private double calculateDistance(Double lat1, Double lon1, Double lat2, Double lon2) {
        final int R = 6371; // Earth's radius in kilometers

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c;

        return distance;
    }
} 