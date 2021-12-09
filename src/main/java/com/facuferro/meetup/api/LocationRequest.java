package com.facuferro.meetup.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
@Builder
@AllArgsConstructor
public class LocationRequest {

    @NotBlank(message = "Location description is mandatory")
    private String description;
    @NotNull(message = "Latitud  is mandatory")
    private Double latitude;
    @NotNull(message = "Longitude is mandatory")
    private Double longitude;

}
