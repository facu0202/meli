package com.facuferro.meetup.domain;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
public class TemperatureBeer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double temperatureFrom;
    private Double temperatureTo;
    private Double countBeer;

    public TemperatureBeer() {
    }

    public TemperatureBeer(Long id, Double temperatureFrom, Double temperatureTo, Double countBeer) {
        this();
        this.id = id;
        this.temperatureFrom = temperatureFrom;
        this.temperatureTo = temperatureTo;
        this.countBeer = countBeer;
    }

    public boolean inRange(Double temperature ){
        return (temperatureFrom==null || temperature > temperatureFrom  )
                && (temperatureTo == null||   temperature <= temperatureTo);
    }

    @Override
    public String toString() {
        return "TemperatureBeer{" +
                "id=" + id +
                ", temperatureFrom=" + temperatureFrom +
                ", temperatureTo=" + temperatureTo +
                ", countBeer=" + countBeer +
                '}';
    }
}
