package com.facuferro.meetup.api;

import com.facuferro.meetup.domain.Role;
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

public class UserResponse {

    private Long id;
    private String name;
    private String surname;
    private String email;
    private Role role;
    private String token;
    private List<UserMeetup> usersMeetup;

    @JsonIgnore
    public static UserResponse createFrom(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .role(user.getRole())
                .token(user.getToken())
                .email(user.getEmail()).build();
    }

}
