package com.facuferro.meetup.meetup.unit.service;

import com.facuferro.meetup.domain.Location;
import com.facuferro.meetup.exception.DataIntegrity;
import com.facuferro.meetup.exception.NotFound;
import com.facuferro.meetup.meetup.unit.util.MeetupAbstractTest;
import com.facuferro.meetup.service.LocationService;
import com.facuferro.meetup.service.LocationServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LocationServiceTest extends MeetupAbstractTest {

    private LocationService locationService;

    @BeforeEach
    public void setup() {

        locationService = new LocationServiceImpl(locationRepository);
    }

    @Test
    public void findAll() {
        when(locationRepository.findAll()).thenReturn(Arrays.asList(location));
        Assertions.assertEquals(locationService.findAll().size(), 1);
    }

    @Test
    public void findById() {
        when(locationRepository.findById(anyLong())).thenReturn(Optional.of(location));
        Assertions.assertNotNull(locationService.findById(1l));
    }

    @Test
    public void findByIdError() {
        when(locationRepository.findById(anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFound.class, () -> locationService.findById(1l));
    }

    @Test
    public void delete() {
        when(locationRepository.findById(anyLong())).thenReturn(Optional.of(location));
        locationService.delete(1l);
    }

    @Test
    public void deleteError() {
        when(locationRepository.findById(anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFound.class, () -> locationService.delete(1l));
    }

    @Test
    public void create() {
        when(locationRepository.save(any(Location.class))).thenReturn(location);
        Location location = locationService.create(getBasicLocationRequest());
        Assertions.assertNotNull(location);
        Assertions.assertNotNull(location.getId());
        Assertions.assertNotNull(location.getDescription());
        Assertions.assertNotNull(location.getLatitude());
        Assertions.assertNotNull(location.getLongitude());

    }


    @Test
    public void createError() {
        when(locationRepository.save(any(Location.class))).thenThrow(super.getMockDataIntegrityException());
        Assertions.assertThrows(DataIntegrity.class, () -> locationService.create(getBasicLocationRequest()));

    }
}
