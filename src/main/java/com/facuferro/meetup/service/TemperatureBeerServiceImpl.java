package com.facuferro.meetup.service;

import com.facuferro.meetup.domain.TemperatureBeer;
import com.facuferro.meetup.exception.DataIntegrity;
import com.facuferro.meetup.exception.NotFound;
import com.facuferro.meetup.repository.TemperatureBeerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
@Validated
public class TemperatureBeerServiceImpl implements TemperatureBeerService {

    private TemperatureBeerRepository temperatureBeerRepository;


    public TemperatureBeer create(TemperatureBeer temperatureBeerRequest) {
        log.info("Creating new temperature beer for request {}", temperatureBeerRequest.toString());
        try {
            return temperatureBeerRepository.save(temperatureBeerRequest);
        } catch (DataIntegrityViolationException exc) {
            throw new DataIntegrity("Error creating Temperature beer", exc);
        }
    }

    @Cacheable("temperatureBeers")
    public List<TemperatureBeer> findAll() {
        log.info("Find all temperature beer");
        return this.temperatureBeerRepository.findAll();
    }


    public TemperatureBeer findById(long id) {
        log.info("Find temperature Beer by id {}", id);
        return this.temperatureBeerRepository.findById(id).orElseThrow(() -> new NotFound("No temperature beer found"));
    }


    public void delete(Long id) {
        log.info("Deleting temperature beer for request {}", id);
        temperatureBeerRepository.delete(this.temperatureBeerRepository.findById(id).orElseThrow(() -> new NotFound("No active temperature beer found for the given ID")));
    }

}
