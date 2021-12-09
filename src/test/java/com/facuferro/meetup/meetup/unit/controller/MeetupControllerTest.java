package com.facuferro.meetup.meetup.unit.controller;

import com.facuferro.meetup.api.MeetupBeerResponse;
import com.facuferro.meetup.api.MeetupRequest;
import com.facuferro.meetup.api.MeetupResponse;
import com.facuferro.meetup.api.UserMeetupResponse;
import com.facuferro.meetup.domain.Meetup;
import com.facuferro.meetup.domain.User;
import com.facuferro.meetup.domain.UserMeetup;
import com.facuferro.meetup.controller.MeetupController;
import com.facuferro.meetup.meetup.unit.util.MeetupAbstractTest;
import com.facuferro.meetup.service.MeetupService;
import com.facuferro.meetup.service.NotificationService;
import com.facuferro.meetup.service.UserMeetupService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MeetupControllerTest extends MeetupAbstractTest {


    @Mock
    protected MeetupService meetupService;
    @Mock
    protected UserMeetupService userMeetupService;
    @Mock
    protected NotificationService notificationService;

    private static long MEETUP_ID=100L;

    private static String TOKEN="Token";

    @Test
    public void beers() {
        MeetupController meetupController = new MeetupController(meetupService,userMeetupService,notificationService);
        when(meetupService.findById(anyLong())).thenReturn(getBasicMeetup(MEETUP_ID));
        when(meetupService.calculateBeer(any(Meetup.class))).thenReturn(100);
        ResponseEntity<MeetupBeerResponse> result =  meetupController.beers(MEETUP_ID);
        assert (result.getBody().getCountBeersBox()==100);
    }

    @Test
    public void temperature() {
        MeetupController meetupController = new MeetupController(meetupService,userMeetupService,notificationService);
        when(meetupService.findById(anyLong())).thenReturn(getBasicMeetup(MEETUP_ID));
        when(meetupService.getTemperature(any(Meetup.class))).thenReturn(20D);
        ResponseEntity<MeetupResponse.MeetupTemperatureResponse> result =  meetupController.temperature(MEETUP_ID);
        assert (result.getBody().getTemperature()==20D);
    }

    @Test
    public void registerInMeeting() {

        MeetupController meetupController = new MeetupController(meetupService,userMeetupService,notificationService);
        UserMeetup userMeetup = getBasicUserMeetup(getBasicUser());
        when(userMeetupService.registerInMeeting(any(User.class),anyLong())).thenReturn(userMeetup);
        doNothing().when(notificationService).notifyInscription(any(UserMeetup.class));
        ResponseEntity<UserMeetupResponse> result = meetupController.registerInMeeting(MEETUP_ID,getBasicUser());
        assert (result.getStatusCode()== HttpStatus.OK);
    }

    @Test
    public void findAll() {
        MeetupController meetupController = new MeetupController(meetupService,userMeetupService,notificationService);
        when(meetupService.findAll()).thenReturn(List.of(getBasicMeetup(MEETUP_ID)));
        ResponseEntity<List<MeetupResponse>> result = meetupController.findAll();
        assert (result.getBody().size()==1);
    }

    @Test
    public void find() {
        MeetupController meetupController = new MeetupController(meetupService,userMeetupService,notificationService);
        when(meetupService.findById(anyLong())).thenReturn(getBasicMeetup(MEETUP_ID));
        ResponseEntity<MeetupResponse> result = meetupController.find(MEETUP_ID);
        assert (result.getBody().getId()==MEETUP_ID);
    }

    @Test
    public void createMeetup() {
        MeetupController meetupController = new MeetupController(meetupService,userMeetupService,notificationService);
        when(meetupService.create(any(MeetupRequest.class))).thenReturn(getBasicMeetup(MEETUP_ID));
        ResponseEntity<MeetupResponse> result = meetupController.createMeetup(getBasicMeetupRequest());
        assert (result.getBody().getId()==MEETUP_ID);
    }

    @Test
    public void confirmRegistration() {
        MeetupController meetupController = new MeetupController(meetupService,userMeetupService,notificationService);
        when(userMeetupService.confirmRegistration(anyLong(),anyString())).thenReturn(getBasicUserMeetup(getBasicUser()));
        meetupController.confirmRegistration(MEETUP_ID,TOKEN);
    }

    @Test
    public void checkin() {
        MeetupController meetupController = new MeetupController(meetupService,userMeetupService,notificationService);
        when(userMeetupService.checkin(any(User.class),anyLong())).thenReturn(getBasicUserMeetup(getBasicUser()));
        meetupController.checkin(MEETUP_ID,getBasicUser(),getBasicConfirmRegistrationRequest(MEETUP_ID,TOKEN));
    }




}
