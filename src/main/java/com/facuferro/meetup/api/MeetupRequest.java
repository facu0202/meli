package com.facuferro.meetup.api;

import com.facuferro.meetup.domain.Location;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class MeetupRequest {

    @NotBlank(message = "Meetup title is mandatory")
    private String title;
    @NotBlank(message = "Meetup description is mandatory")
    private String description;
    @NotNull(message = "Meetup Location is mandatory")
    private Location location;
    @NotNull(message = "Meetup date is mandatory")
    private Date date;
    @NotNull(message = "Meetup limitDate is mandatory")
    private Date limitDate;
    @NotNull(message = "Meetup duration is mandatory")
    private int duration;

    @Override
    public String toString() {
        return "MeetupRequest{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", location=" + location +
                ", date=" + date +
                ", limitDate=" + limitDate +
                ", duration=" + duration +
                '}';
    }
}
