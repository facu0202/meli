package com.facuferro.meetup.meetup.unit.controller;

import com.facuferro.meetup.api.UserRequest;
import com.facuferro.meetup.api.UserResponse;
import com.facuferro.meetup.domain.User;
import com.facuferro.meetup.domain.UserState;
import com.facuferro.meetup.controller.UserController;
import com.facuferro.meetup.meetup.unit.util.MeetupAbstractTest;
import com.facuferro.meetup.service.NotificationService;
import com.facuferro.meetup.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest extends MeetupAbstractTest {

    public static long USER_ID = 1l;
    public static String TOKEN = "TOKEN";
    @Mock
    protected UserService userService;

    @Mock
    protected NotificationService notificationService;

    @Test
    public void registerUser() {
        UserController userController = new UserController(userService,notificationService);
        when(userService.create(any(UserRequest.class),any(UserState.class))).thenReturn(getBasicUser());
        ResponseEntity<UserResponse> result = userController.registerUser(getBasicUserRequest());
        assert (result.getStatusCode()== HttpStatus.OK);
        Assertions.assertNotNull(result.getBody().getEmail());
        Assertions.assertNotNull(result.getBody().getId());
        Assertions.assertNotNull(result.getBody().getName());
        Assertions.assertNotNull(result.getBody().getSurname());
        Assertions.assertNotNull(result.getBody().getRole());
        Assertions.assertNotNull(result.getBody().getToken());
        Assertions.assertNull(result.getBody().getUsersMeetup());
    }

    @Test
    public void findAll() {
        UserController userController = new UserController(userService,notificationService);
        when(userService.findAll()).thenReturn(Arrays.asList(getBasicUser()));
        ResponseEntity<List<UserResponse>>  result = userController.findAll();
        assert (result.getStatusCode()== HttpStatus.OK);
    }

    @Test
    public void find() {
        UserController userController = new UserController(userService,notificationService);
        when(userService.findById(anyLong())).thenReturn(getBasicUser());
        ResponseEntity<UserResponse> result = userController.find(USER_ID);
        assert (result.getStatusCode()== HttpStatus.OK);
    }

    @Test
    public void confirmRegistration() {
        UserController userController = new UserController(userService,notificationService);
        when(userService.confirm(any(User.class),anyString())).thenReturn(getBasicUser());
        userController.confirmRegistration(USER_ID,TOKEN);
    }


}
