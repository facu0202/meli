package com.facuferro.meetup.service;

import com.facuferro.meetup.domain.User;
import com.facuferro.meetup.domain.UserMeetup;


public interface NotificationService {

    void notifyUserAndAdmins();
    void notifyNewUser(User user);
    void notifyInscription(UserMeetup userMeetup);

}
