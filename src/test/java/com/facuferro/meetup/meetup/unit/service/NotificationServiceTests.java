package com.facuferro.meetup.meetup.unit.service;

import com.facuferro.meetup.domain.Meetup;
import com.facuferro.meetup.domain.Role;
import com.facuferro.meetup.domain.UserMeetup;
import com.facuferro.meetup.helper.BeersHelperImpl;
import com.facuferro.meetup.meetup.unit.util.MeetupAbstractTest;
import com.facuferro.meetup.service.*;
import com.facuferro.meetup.validator.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTests extends MeetupAbstractTest {

	private NotificationService notificationService;
	private NotificationService notificationServiceSender;
	private UserMeetupService userMeetupService;
	@Mock
	private JavaMailSender sender;
	private EmailService emailService = new EmailServiceImpl(sender, false);
	private EmailService emailServiceSender = new EmailServiceImpl(sender, false);
	private JavaMailSender javaMailSender;
	private MimeMessage mimeMessage;

	@BeforeEach
	void setup() {

		mimeMessage = new MimeMessage((Session)null);
		javaMailSender = mock(JavaMailSender.class);

		emailService = new EmailServiceImpl(javaMailSender,false);
		emailServiceSender = new EmailServiceImpl(javaMailSender,true);

		meetupService = new MeetupServiceImpl(meetupRepository, weatherService,new BeersHelperImpl(temperatureBeerService));
		userMeetupService = new UserMeetupServiceImpl(userMeetupRepository,meetupRepository,meetupService,userMeetupValidator);
		UserService userService = new UserServiceImpl(userRepository,new UserValidator());
		notificationService = new NotificationServiceImpl(meetupService,userMeetupService,userService,emailService);
		notificationServiceSender = new NotificationServiceImpl(meetupService,userMeetupService,userService,emailServiceSender);
		meetupService = new MeetupServiceImpl(meetupRepository, weatherService,new BeersHelperImpl(temperatureBeerService));
		meetup = getBasicMeetup(1l);
		meetup.setUsersMeetup(getUserMeetup(meetup,10));
	}

	@Test
	void notifyUserAndAdmins(){
		when(meetupRepository.getAllBetweenDates(any(Date.class),any(Date.class))).thenReturn(Arrays.asList(meetup));
		when(userRepository.findByRole(any(Role.class))).thenReturn(Arrays.asList(getBasicUser()));
		when(userMeetupRepository.findByMeetupAndNotified(any(Meetup.class),any(Boolean.class))).thenReturn(Arrays.asList(getBasicUserMeetup(getBasicUser())));
		notificationService.notifyUserAndAdmins();
	}

	@Test
	void notifyUserAndAdminsSender(){
		when(javaMailSender.createMimeMessage()).thenThrow(new RuntimeException("Error"));
		when(meetupRepository.getAllBetweenDates(any(Date.class),any(Date.class))).thenReturn(Arrays.asList(meetup));
		when(userRepository.findByRole(any(Role.class))).thenReturn(Arrays.asList(getBasicUser()));
		when(userMeetupRepository.findByMeetupAndNotified(any(Meetup.class),any(Boolean.class))).thenReturn(Arrays.asList(getBasicUserMeetup(getBasicUser())));
		notificationServiceSender.notifyUserAndAdmins();
	}

	@Test
	void notifyUserAndAdminsError(){
		when(meetupRepository.getAllBetweenDates(any(Date.class),any(Date.class))).thenReturn(Arrays.asList(meetup));
		when(userRepository.findByRole(any(Role.class))).thenReturn(Arrays.asList(getBasicUser()));
		when(userMeetupRepository.findByMeetupAndNotified(any(Meetup.class),any(Boolean.class))).thenReturn(Arrays.asList(getBasicUserMeetup(getBasicUser())));
		when(meetupRepository.save(any(Meetup.class))).thenThrow(getMockDataIntegrityException());
		when(userMeetupRepository.save(any(UserMeetup.class))).thenThrow (getMockDataIntegrityException());
		notificationService.notifyUserAndAdmins();
	}

	@Test
	void notifyUserAndAdminsEmpty(){
		meetup.setNotified(true);
		when(meetupRepository.getAllBetweenDates(any(Date.class),any(Date.class))).thenReturn(Arrays.asList(meetup));
		when(userMeetupRepository.findByMeetupAndNotified(any(Meetup.class),any(Boolean.class))).thenReturn(new ArrayList<>());
		notificationService.notifyUserAndAdmins();
	}

	@Test
	void notifyNewUser(){
		notificationService.notifyNewUser(getBasicUser());
	}

	@Test
	void notifyNewUserSenderError(){
		when(javaMailSender.createMimeMessage()).thenThrow(new RuntimeException("Error"));
		notificationServiceSender.notifyNewUser(getBasicUser());
	}


	@Test
	void notifyInscription(){
		notificationService.notifyInscription(getBasicUserMeetup(getBasicUser()));
	}

	@Test
	void notifyInscriptionSender(){
		when(javaMailSender.createMimeMessage()).thenThrow(new RuntimeException("Error"));
		notificationServiceSender.notifyInscription(getBasicUserMeetup(getBasicUser()));
	}




}
