package com.facuferro.meetup.api;

import com.facuferro.meetup.domain.Meetup;
import com.facuferro.meetup.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ConfirmRegistrationRequest {

    private Meetup meetup;
    private User user;
    private String token;

}
