package com.facuferro.meetup.service;

import com.facuferro.meetup.api.LocationRequest;
import com.facuferro.meetup.domain.Location;
import com.facuferro.meetup.exception.DataIntegrity;
import com.facuferro.meetup.exception.NotFound;
import com.facuferro.meetup.repository.LocationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
@Validated
public class LocationServiceImpl implements LocationService {

    private LocationRepository locationRepository;

    public Location create(LocationRequest locationRequest) {
        log.info("Creating new Location for request {}", locationRequest.toString());
        Location newLocation = generateLocation(locationRequest);
        try {
            return locationRepository.save(newLocation);
        } catch (DataIntegrityViolationException exc) {
            log.error("Concurrent Location", exc);
            throw new DataIntegrity("Error creating Location", exc);
        }
    }

    public List<Location> findAll() {
        log.info("Location findAll");
        return this.locationRepository.findAll();
    }


    public Location findById(Long id) {
        log.info("Location find by Id {}", id);
        return this.locationRepository.findById(id).orElseThrow(() -> new NotFound("Location no found"));
    }

    private Location generateLocation(LocationRequest locationRequest) {
        return Location.builder()
                .description(locationRequest.getDescription())
                .latitude(locationRequest.getLatitude())
                .longitude(locationRequest.getLatitude())
                .build();
    }

    public void delete(Long id) {
        log.info("Deleting Location for request {}", id);
        locationRepository.delete(this.locationRepository.findById(id).orElseThrow(() -> new NotFound("No active Location found for the given ID")));
    }

}

