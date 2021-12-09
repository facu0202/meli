package com.facuferro.meetup.repository;


import com.facuferro.meetup.domain.Meetup;
import com.facuferro.meetup.domain.User;
import com.facuferro.meetup.domain.UserMeetup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserMeetupRepository extends JpaRepository<UserMeetup, Long> {

    Optional<UserMeetup> findFirstByUserAndToken(User user, String token);
    Optional<UserMeetup> findFirstByMeetupAndToken(Meetup meetup, String token);
    Optional<UserMeetup> findFirstByUserAndMeetup(User user, Meetup meetup);
    List<UserMeetup> findByMeetupAndNotified(Meetup meetup,Boolean notified);
}