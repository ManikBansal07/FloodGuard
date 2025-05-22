package com.floodguard.repository;

import com.floodguard.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    List<Location> findByNameContainingIgnoreCaseOrAddressContainingIgnoreCase(String name, String address);
    boolean existsByLatitudeAndLongitude(Double latitude, Double longitude);
} 