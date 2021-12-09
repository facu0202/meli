package com.facuferro.meetup.meetup.unit.service;

import com.facuferro.meetup.domain.TemperatureBeer;
import com.facuferro.meetup.meetup.unit.util.MeetupAbstractTest;
import com.facuferro.meetup.exception.DataIntegrity;
import com.facuferro.meetup.exception.NotFound;
import com.facuferro.meetup.repository.TemperatureBeerRepository;
import com.facuferro.meetup.service.TemperatureBeerService;
import com.facuferro.meetup.service.TemperatureBeerServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TemperatureBeerServiceTest extends MeetupAbstractTest {

    @Mock
    private TemperatureBeerRepository temperatureBeerRepository;
    private TemperatureBeerService temperatureBeerService;

    @BeforeEach
    public void setup() {

        temperatureBeerService = new TemperatureBeerServiceImpl(temperatureBeerRepository);
    }

    @Test
    public void findAll() {
        when(temperatureBeerRepository.findAll()).thenReturn(getTemperatures());
        Assertions.assertEquals(temperatureBeerService.findAll().size(), 3);
    }

    @Test
    public void findById() {
        when(temperatureBeerRepository.findById(anyLong())).thenReturn(Optional.of(getBasicTemperature()));
        Assertions.assertNotNull(temperatureBeerService.findById(1l));
    }

    @Test
    public void findByIdError() {
        when(temperatureBeerRepository.findById(anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFound.class, () -> temperatureBeerService.findById(1l));
    }

    @Test
    public void delete() {
        when(temperatureBeerRepository.findById(anyLong())).thenReturn(Optional.of(getBasicTemperature()));
        temperatureBeerService.delete(1l);
    }

    @Test
    public void deleteError() {
        when(temperatureBeerRepository.findById(anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFound.class, () -> temperatureBeerService.delete(1l));
    }

    @Test
    public void create() {
        when(temperatureBeerRepository.save(any(TemperatureBeer.class))).thenReturn(getBasicTemperature());
        TemperatureBeer temperatureBeer = temperatureBeerService.create(getBasicTemperature());
        Assertions.assertNotNull(temperatureBeer);
        Assertions.assertNotNull(temperatureBeer.getTemperatureTo());
        Assertions.assertNull(temperatureBeer.getTemperatureFrom());
        Assertions.assertNotNull(temperatureBeer.getCountBeer());
        Assertions.assertNull(temperatureBeer.getId());
    }


    @Test
    public void createError() {
        when(temperatureBeerRepository.save(any(TemperatureBeer.class))).thenThrow(super.getMockDataIntegrityException());
        Assertions.assertThrows(DataIntegrity.class, () -> temperatureBeerService.create(getBasicTemperature()));

    }


}
