package com.facuferro.meetup.service;

import com.facuferro.meetup.api.UserMeetupRequest;
import com.facuferro.meetup.domain.Meetup;
import com.facuferro.meetup.domain.User;
import com.facuferro.meetup.domain.UserMeetup;

import java.util.List;


public interface UserMeetupService {


    UserMeetup registerInMeeting(User user, long id);

    UserMeetup registerInMeetup(UserMeetupRequest userMeetupRequest);

    UserMeetup confirmRegistration(long id, String token);

    UserMeetup checkin(User user,long id);

    List<UserMeetup> getUserMeetups(Meetup m);

    void notified(UserMeetup u);

}
