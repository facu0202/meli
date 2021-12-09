package com.facuferro.meetup.meetup.unit.helper;

import com.facuferro.meetup.domain.TemperatureBeer;
import com.facuferro.meetup.helper.BeersHelper;
import com.facuferro.meetup.helper.BeersHelperImpl;
import com.facuferro.meetup.meetup.unit.util.MeetupAbstractTest;
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

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BeersHelperTest extends MeetupAbstractTest {

    @Mock
    private TemperatureBeerRepository temperatureBeerRepository;
    @Mock
    private TemperatureBeerService temperatureBeerService;
    private BeersHelper beersHelper;

    @BeforeEach
    public void setup() {
        temperatureBeerService = new TemperatureBeerServiceImpl(temperatureBeerRepository);
        when(temperatureBeerRepository.findAll()).thenReturn(getTemperatures());
        beersHelper = new BeersHelperImpl(temperatureBeerService);
    }

    @Test
    void countBeerBoxs (){

        Optional<TemperatureBeer> temperatureBeer = temperatureBeerService.findAll().stream().filter(t -> t.inRange(10d)).findFirst();
        Assertions.assertTrue(temperatureBeer.isPresent());
        Assertions.assertEquals(temperatureBeer.get().getCountBeer(),0.75d);

        temperatureBeer = temperatureBeerService.findAll().stream().filter(t -> t.inRange(20d)).findFirst();
        Assertions.assertTrue(temperatureBeer.isPresent());
        Assertions.assertEquals(temperatureBeer.get().getCountBeer(),0.75d);

        temperatureBeer = temperatureBeerService.findAll().stream().filter(t -> t.inRange(20.01d)).findFirst();
        Assertions.assertTrue(temperatureBeer.isPresent());
        Assertions.assertEquals(temperatureBeer.get().getCountBeer(),1d);

        temperatureBeer = temperatureBeerService.findAll().stream().filter(t -> t.inRange(23d)).findFirst();
        Assertions.assertTrue(temperatureBeer.isPresent());
        Assertions.assertEquals(temperatureBeer.get().getCountBeer(),1d);


        temperatureBeer = temperatureBeerService.findAll().stream().filter(t -> t.inRange(24d)).findFirst();
        Assertions.assertTrue(temperatureBeer.isPresent());
        Assertions.assertEquals(temperatureBeer.get().getCountBeer(),1d);

        temperatureBeer = temperatureBeerService.findAll().stream().filter(t -> t.inRange(24.1d)).findFirst();
        Assertions.assertTrue(temperatureBeer.isPresent());
        Assertions.assertEquals(temperatureBeer.get().getCountBeer(),2d);


        temperatureBeer = temperatureBeerService.findAll().stream().filter(t -> t.inRange(100d)).findFirst();
        Assertions.assertTrue(temperatureBeer.isPresent());
        Assertions.assertEquals(temperatureBeer.get().getCountBeer(),2d);

    }

    @Test
    void getTemperature (){


        Assertions.assertEquals(beersHelper.countBeerBoxs(1,15),1);
        Assertions.assertEquals(beersHelper.countBeerBoxs(1,20),1);
        Assertions.assertEquals(beersHelper.countBeerBoxs(1,24.1),1);
    }
}
