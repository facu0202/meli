package com.facuferro.meetup.provider;


import com.facuferro.meetup.domain.Meetup;
import com.facuferro.meetup.provider.openweathermap.WeatherRoot;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

@Service
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@Validated
public class WeatherService {
    @Autowired
    private RestTemplate restTemplate;
    @Value("${api.openweathermap.org.api.key}")
    private String apiKey;
    @Value( "${weatherService.url}" )
    private String jdbcUrl;

    @Cacheable(value = "temperatures", key = "#meetup.id")
    public Double getTemperature(Meetup meetup) {
        return this.getTemperature(meetup.getLocation().getLatitude(), meetup.getLocation().getLatitude(), meetup.getDate());
    }

    public Double getTemperature(double latitud, double longitud, Date date) {
        String url = jdbcUrl;
        return restTemplate.getForObject(url, WeatherRoot.class, latitud, longitud, apiKey).getMain().getTemp();
    }
}
