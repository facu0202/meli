package com.facuferro.meetup.api;

import com.facuferro.meetup.domain.User;
import com.facuferro.meetup.domain.UserMeetup;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class UserOAuthResponse {

    private Long id;
    private String name;
    private String surname;
    private String email;
    private String token;
    private List<UserMeetup> usersMeetup;

    @JsonIgnore
    public static UserOAuthResponse createFrom(User user) {
        return UserOAuthResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .token(user.getToken())
                .email(user.getEmail()).build();
    }

}
