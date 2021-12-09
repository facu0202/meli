package com.facuferro.meetup.service;

import com.facuferro.meetup.api.LocationRequest;
import com.facuferro.meetup.domain.Location;

import java.util.List;


public interface LocationService {
     Location create(LocationRequest locationRequest);
     List<Location> findAll();
     Location findById(Long id);
     void delete(Long id);
}