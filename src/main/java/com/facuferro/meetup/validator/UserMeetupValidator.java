package com.facuferro.meetup.validator;

import com.facuferro.meetup.domain.Meetup;
import com.facuferro.meetup.domain.MeetupState;
import com.facuferro.meetup.domain.User;
import com.facuferro.meetup.domain.UserMeetup;
import com.facuferro.meetup.exception.MeetupException;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class UserMeetupValidator {

    public void validateToRegister(Meetup meetup, User user){

        if (meetup.getStatus()!= MeetupState.ACTIVE)
             throw new MeetupException("Meetup state no support register");
        if (!meetup.getLimitDate().after(new Date()))
            throw new MeetupException("The date of the meeting does not allow registrations");
        if (!user.isActive())
            throw new MeetupException("User state no support register");
    }

    public void validateToConfirm(UserMeetup userMeetup){
        if (!userMeetup.isPending())
            throw new MeetupException("User meetup state no support confirmation");
    }

    public void validateToCheckin(UserMeetup userMeetup){
        if (!userMeetup.isActive())
            throw new MeetupException("User meetup state no support checkin");
    }


}
