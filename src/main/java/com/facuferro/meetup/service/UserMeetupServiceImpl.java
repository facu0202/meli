package com.facuferro.meetup.service;

import com.facuferro.meetup.api.UserMeetupRequest;
import com.facuferro.meetup.domain.Meetup;
import com.facuferro.meetup.domain.User;
import com.facuferro.meetup.domain.UserMeetup;
import com.facuferro.meetup.domain.UserMeetupState;
import com.facuferro.meetup.exception.DataIntegrity;
import com.facuferro.meetup.exception.MeetupException;
import com.facuferro.meetup.repository.MeetupRepository;
import com.facuferro.meetup.repository.UserMeetupRepository;
import com.facuferro.meetup.validator.UserMeetupValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class UserMeetupServiceImpl implements UserMeetupService {

    private UserMeetupRepository userMeetupRepository;
    private MeetupRepository meetupRepository;
    private MeetupService meetupService;
    private UserMeetupValidator userMeetupValidator;

    public UserMeetup registerInMeeting(User user, long id) {
        log.info("Register user meetup for request {}", id);
        Meetup meetup = meetupService.findById(id);
        UserMeetup newUserMeetup = getUserMeetup(user, meetup);
        userMeetupValidator.validateToRegister(meetup, user);
        try {
            newUserMeetup = userMeetupRepository.save(newUserMeetup);
            return newUserMeetup;
        } catch (DataIntegrityViolationException exc) {
            throw new DataIntegrity("Error register user meetup state", exc);
        }
    }

    private UserMeetup getUserMeetup(User user, Meetup meetup) {
        return UserMeetup.builder().meetup(meetup).user(user).token(UUID.randomUUID().toString()).status(UserMeetupState.PENDING).notified(false).build();
    }

    public UserMeetup registerInMeetup(UserMeetupRequest userMeetupRequest) {
        log.info("Creating new usermeetup for request {}", userMeetupRequest.toString());
        UserMeetup newUserMeetup = generateUserMeetup(userMeetupRequest);
        try {
            return userMeetupRepository.save(newUserMeetup);
        } catch (DataIntegrityViolationException exc) {
            throw new DataIntegrity("Error register user meetup state", exc);
        }
    }

    private UserMeetup generateUserMeetup(UserMeetupRequest userMeetupRequest) {
        return UserMeetup.builder()
                .user(userMeetupRequest.getUser())
                .meetup(userMeetupRequest.getMeetup())
                .status(UserMeetupState.ACTIVE)
                .notified(false)
                .token(UUID.randomUUID().toString())
                .build();
    }

    public UserMeetup confirmRegistration(long id, String token) {
        log.info("Confirm user meetup for request {}", id);
        UserMeetup userMeetup = userMeetupRepository.findFirstByMeetupAndToken(Meetup.builder().id(id).build(), token).orElseThrow(() -> new MeetupException("Meetup user registration found"));
        userMeetupValidator.validateToConfirm(userMeetup);
        userMeetup.toActive();
        try {
            return userMeetupRepository.save(userMeetup);
        } catch (DataIntegrityViolationException exc) {
            throw new DataIntegrity("Error changing user meetup state", exc);
        }
    }

    public UserMeetup checkin(User user, long id) {
        log.info("Checkin user meetup for request {}", user.toString());
        Meetup meetup = meetupService.findById(id);
        UserMeetup userMeetup = userMeetupRepository.findFirstByUserAndMeetup(user, meetup).orElseThrow(() -> new MeetupException("Meetup user registration found"));
        userMeetupValidator.validateToCheckin(userMeetup);
        userMeetup.setStatus(UserMeetupState.CHECK_IN);
        try {
            return userMeetupRepository.save(userMeetup);
        } catch (DataIntegrityViolationException exc) {
            throw new DataIntegrity("Error changing user meetup state", exc);
        }
    }


    public List<UserMeetup> getUserMeetups(Meetup meetup) {
        log.info("Get meetups to notify");
        return userMeetupRepository.findByMeetupAndNotified(meetup, false);
    }

    public void notified(UserMeetup u) {
        u.setNotified(true);
        userMeetupRepository.save(u);
    }

}
