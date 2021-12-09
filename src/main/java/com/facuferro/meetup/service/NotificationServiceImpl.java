package com.facuferro.meetup.service;

import com.facuferro.meetup.domain.Meetup;
import com.facuferro.meetup.domain.User;
import com.facuferro.meetup.domain.UserMeetup;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.SchedulerLock;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private MeetupService meetupService;
    private UserMeetupService userMeetUpService;
    private UserService userService;
    private EmailService emailService;
    private static final String BASE_ERROR_SEND_EMAIL =  "Error was sending to {}";


    public void notifyNewUser(User user) {
        log.info("Notify new user ");
        this.send(user);
        log.info("End notify new user ");
    }

    public void notifyInscription(UserMeetup userMeetup) {
        log.info("Notify user registration ");
        this.send(userMeetup);
        log.info("End notify user registration ");
    }

    @Scheduled(cron = "*/60 * * * * *")
    @SchedulerLock(name = "Notification", lockAtLeastForString = "PT5M", lockAtMostForString = "PT5M")
    public void notifyUserAndAdmins() {
        log.info("Run notification users and administrators");
        List<Meetup> meetups = meetupService.getMeetupsToNotify();
        meetups.forEach(m -> notify(m));
        log.info("End run notification users and administrators");
    }

    private void notify(Meetup meetup) {
        notifyUsers(meetup);
        notifyAdministrator(meetup);
    }

    private void notifyUsers(Meetup meetup) {
        log.info("Notify user meetup ");
        List<UserMeetup> users = userMeetUpService.getUserMeetups(meetup).stream().filter(u -> !u.getNotified()).collect(Collectors.toList());
        if (users.isEmpty())
            log.info("Users were notified {}", meetup);
        else {
            users.forEach(u -> send(meetup, u));
            log.info("End users notify meetup " + users);
        }
    }

    private void notifyAdministrator(Meetup meetup) {
        log.info("Notify administrators");
        if (meetup.getNotified()) {
            log.info("Meetup was notified {}", meetup);
        } else {
            List<User> users = userService.findAllAdmins();
            users.forEach(u -> send(meetup, u));
            log.info("End notify administrators ");
        }
    }

    @Async
    public void send(UserMeetup userMeetup) {
        try {
            emailService.sendEmailTool("Ustede se ha inscripto en la meetup. Para confirmar su asistenncia ingrese al siguiente link: ....", userMeetup.getUser().getEmail(), "Inscripcion meetup");
        } catch (Exception e) {
            log.error(BASE_ERROR_SEND_EMAIL, userMeetup.getId(), e);
        }
    }

    @Async
    public void send(User user) {
        try {
            emailService.sendEmailTool("Bienvenido a meetup. Para confirmar su cuenta ingrese al siguiente link: ....", user.getEmail(), "Bienvenido a meetup");
            user.toActive();
        } catch (Exception e) {
            log.error(BASE_ERROR_SEND_EMAIL, user.getEmail(), e);
        }
    }

    @Async
    public void send(Meetup m, User u) {
        try {
            emailService.sendEmailTool("No te olvides de la meetup", u.getEmail(), m.getTitle());
            meetupService.notified(m);
        } catch (Exception e) {
            log.error(BASE_ERROR_SEND_EMAIL, u.getEmail(), e);
        }
    }

    @Async
    public void send(Meetup m, UserMeetup u) {
        try {
            emailService.sendEmailTool("No te olvides de la meetup", u.getUser().getEmail(), m.getTitle());
            userMeetUpService.notified(u);
        } catch (Exception e) {
            log.error(BASE_ERROR_SEND_EMAIL, u.getUser().getEmail(), e);
        }
    }
}
