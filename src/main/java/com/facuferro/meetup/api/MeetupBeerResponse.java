package com.facuferro.meetup.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MeetupBeerResponse {


    private Integer countBeersBox;

    @JsonIgnore
    public static MeetupBeerResponse createFrom(Integer countBeersBox) {
        return MeetupBeerResponse.builder()
                .countBeersBox(countBeersBox).build();
    }

}
