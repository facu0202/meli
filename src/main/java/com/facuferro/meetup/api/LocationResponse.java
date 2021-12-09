package com.facuferro.meetup.api;

import com.facuferro.meetup.domain.Location;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
@AllArgsConstructor
public class LocationResponse {

    private Long id;
    private String description;
    private Double latitude;
    private Double longitude;

    @JsonIgnore
    public static LocationResponse createFrom(Location location) {
        return LocationResponse.builder()
                .description(location.getDescription())
                .id(location.getId())
                .latitude(location.getLatitude())
                .longitude(location.getLongitude()).build();
    }
}
