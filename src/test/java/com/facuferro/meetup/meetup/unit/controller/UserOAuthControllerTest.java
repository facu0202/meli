package com.facuferro.meetup.meetup.unit.controller;


import com.facuferro.meetup.api.UserOAuthResponse;
import com.facuferro.meetup.controller.UserOAuthController;
import com.facuferro.meetup.meetup.unit.util.MeetupAbstractTest;
import com.facuferro.meetup.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
public class UserOAuthControllerTest extends MeetupAbstractTest {

    @Mock
    protected UserService userService;

    @Test
    public void login() {

        UserOAuthController userOAuthController = new UserOAuthController(userService);
        when(userService.getAndVerify(anyString(),anyString())).thenReturn(getBasicUser());
        ResponseEntity<UserOAuthResponse> result = userOAuthController.login("facu","facu");
        assert (result.getStatusCode()== HttpStatus.OK);

    }



}
