package com.facuferro.meetup.api;

import com.facuferro.meetup.domain.Location;
import com.facuferro.meetup.domain.Meetup;
import com.facuferro.meetup.domain.MeetupState;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
public class MeetupResponse {
    private Long id;
    private String title;
    private String description;
    private Location location;
    private MeetupState status;
    private Date date;
    private Date limitDate;
    private Integer duration;
    private List<UserMeetupResponse> usersMeetup;

    @JsonIgnore
    public static MeetupResponse createFrom(Meetup meetup) {
        return MeetupResponse.builder()
                .id(meetup.getId())
                .title(meetup.getTitle())
                .description(meetup.getDescription())
                .status(meetup.getStatus())
                .date(meetup.getDate())
                .location(meetup.getLocation())
                .limitDate(meetup.getLimitDate())
                .usersMeetup(meetup.getUsersMeetup() == null ? new ArrayList<>() : meetup.getUsersMeetup().stream().map(UserMeetupResponse::createFromWithoutMeetUp).collect(Collectors.toList()))
                .duration(meetup.getDuration()).build();
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class MeetupTemperatureResponse {


        private Double temperature;

        @JsonIgnore
        public static MeetupTemperatureResponse createFrom(Double temperature) {
            return MeetupTemperatureResponse.builder()
                    .temperature(temperature).build();
        }

    }
}
