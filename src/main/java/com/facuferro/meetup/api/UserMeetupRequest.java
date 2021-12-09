package com.facuferro.meetup.api;

import com.facuferro.meetup.domain.Meetup;
import com.facuferro.meetup.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
public class UserMeetupRequest {
    @NotNull(message = "meetup is mandatory")
    private Meetup meetup;
    @NotNull(message = "user is mandatory")
    private User user;

    @Override
    public String toString() {
        return "UserMeetupRequest{" +
                "meetup=" + meetup.getId() +
                ", user=" + user.getId() +
                '}';
    }
}
