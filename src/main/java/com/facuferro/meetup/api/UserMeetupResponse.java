package com.facuferro.meetup.api;

import com.facuferro.meetup.domain.Meetup;
import com.facuferro.meetup.domain.UserMeetup;
import com.facuferro.meetup.domain.UserMeetupState;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UserMeetupResponse {

    private Long id;
    private Meetup meetup;
    private UserResponse user;
    private String token;
    private UserMeetupState status;

    @JsonIgnore
    public static UserMeetupResponse createFrom(UserMeetup userMeetup) {
        return UserMeetupResponse.builder()
                .id(userMeetup.getId())
                .token(userMeetup.getToken())
                .status(userMeetup.getStatus()).build();
    }

    @JsonIgnore
    public static UserMeetupResponse createFromWithoutMeetUp(UserMeetup userMeetup) {
        return UserMeetupResponse.builder()
                .id(userMeetup.getId())
                .token(userMeetup.getToken())
                .user(UserResponse.createFrom(userMeetup.getUser()))
                .status(userMeetup.getStatus()).build();
    }

}
