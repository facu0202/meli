package com.facuferro.meetup.service;

import com.facuferro.meetup.domain.TemperatureBeer;

import java.util.List;


public interface TemperatureBeerService {


    TemperatureBeer create(TemperatureBeer temperatureBeerRequest);

    List<TemperatureBeer> findAll();


    TemperatureBeer findById(long id);


    void delete(Long id);

}
