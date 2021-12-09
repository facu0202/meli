package com.facuferro.meetup.helper;

import com.facuferro.meetup.domain.TemperatureBeer;
import com.facuferro.meetup.exception.MeetupException;
import com.facuferro.meetup.service.TemperatureBeerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

@Component
@AllArgsConstructor
@Slf4j
@Validated
public class BeersHelperImpl implements BeersHelper {
    private TemperatureBeerService temperatureBeerService;

    private Double getCount(double temp) {
        Optional<TemperatureBeer> temperatureBeer = temperatureBeerService.findAll().stream().filter(t -> t.inRange(temp)).findFirst();
        if (temperatureBeer.isPresent())
            return BigDecimal.valueOf(temperatureBeer.get().getCountBeer()).setScale(0, RoundingMode.UP).doubleValue();
        else
            throw new MeetupException("Temperature is wrong or it is not present in the valid ranger");
    }


    public int countBeerBoxs(long user, double temp) {
        double countBeers = getCount(temp);
        double totalBeers = countBeers * user;
        return BigDecimal.valueOf(totalBeers / 6).setScale(0, RoundingMode.UP).intValue();
    }


}
