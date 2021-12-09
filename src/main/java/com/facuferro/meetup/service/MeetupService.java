package com.facuferro.meetup.service;

import com.facuferro.meetup.api.MeetupRequest;
import com.facuferro.meetup.domain.Meetup;

import java.util.List;


public interface MeetupService {

    Meetup findById(Long id);

    int calculateBeer(Meetup meetup);

    Double getTemperature(Meetup meetup);

    List<Meetup> findAll();

    Meetup create(MeetupRequest meetupRequest);

    void notified(Meetup u);

    void closedMeetups();

    List<Meetup> getMeetupsToNotify();
}
