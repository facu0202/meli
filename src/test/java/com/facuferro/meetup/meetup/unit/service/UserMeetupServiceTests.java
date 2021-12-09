package com.facuferro.meetup.meetup.unit.service;

import com.facuferro.meetup.api.UserMeetupRequest;
import com.facuferro.meetup.domain.Meetup;
import com.facuferro.meetup.domain.User;
import com.facuferro.meetup.domain.UserMeetup;
import com.facuferro.meetup.domain.UserMeetupState;
import com.facuferro.meetup.exception.DataIntegrity;
import com.facuferro.meetup.exception.MeetupException;
import com.facuferro.meetup.helper.BeersHelperImpl;
import com.facuferro.meetup.meetup.unit.util.MeetupAbstractTest;
import com.facuferro.meetup.service.MeetupServiceImpl;
import com.facuferro.meetup.service.UserMeetupService;
import com.facuferro.meetup.service.UserMeetupServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserMeetupServiceTests extends MeetupAbstractTest {
	UserMeetupService userMeetupService;

	@BeforeEach
	public void setup() {
		meetupService = new MeetupServiceImpl(meetupRepository, weatherService,new BeersHelperImpl(temperatureBeerService));
		userMeetupService = new UserMeetupServiceImpl(userMeetupRepository,meetupRepository,meetupService,userMeetupValidator);
		meetup = getBasicMeetup(1l);
		meetup.setUsersMeetup(getUserMeetup(meetup,10));
	}


	@Test
	void registerInMeetingOk() {
		User user = getBasicUser();
		UserMeetup userMeetup = getBasicUserMeetup(user);
		when(meetupRepository.findById(anyLong())).thenReturn(Optional.of(meetup));
		when(userMeetupRepository.save(any(UserMeetup.class))).thenReturn(userMeetup);
		UserMeetup um = userMeetupService.registerInMeeting(user,1);
		Assertions.assertNotNull(um);
		Assertions.assertNotNull(um.getMeetup());
	}

	@Test
	void generateUserMeetupOk() {

		User user = getBasicUser();
		UserMeetup userMeetup = getBasicUserMeetup(user);
		when(userMeetupRepository.save(any(UserMeetup.class))).thenReturn(userMeetup);
		UserMeetupRequest userMeetupRequest = new UserMeetupRequest(meetup,user) ;
		UserMeetup um = userMeetupService.registerInMeetup(userMeetupRequest);
		Assertions.assertNotNull(um);
	}
	@Test
	void registerInMeetingError() {
		User user = getBasicUser();
		UserMeetup userMeetup = getBasicUserMeetup(user);
		when(meetupRepository.findById(anyLong())).thenReturn(Optional.of(meetup));
		when(userMeetupRepository.save(any(UserMeetup.class))).thenThrow (getMockDataIntegrityException());

		DataIntegrity exception = assertThrows(DataIntegrity.class, () -> {
			userMeetupService.registerInMeeting(user,1);
		});
		String expectedMessage = "Error register user meetup state";
		String actualMessage = exception.getMessage();
		Assertions.assertTrue(actualMessage.contains(expectedMessage));
	}
	@Test
	void checkinOk() {
		User user = getBasicUser();
		UserMeetup userMeetup = getBasicUserMeetup(user);
		when(meetupRepository.findById(anyLong())).thenReturn(Optional.of(meetup));
		when(userMeetupRepository.save(any(UserMeetup.class))).thenReturn(userMeetup);
		when(userMeetupRepository.findFirstByUserAndMeetup(any(User.class),any(Meetup.class))).thenReturn(Optional.of(userMeetup));
		UserMeetup um = userMeetupService.checkin(user,1l);
		Assertions.assertNotNull(um);
	}

	@Test
	void checkinError() {
		User user = getBasicUser();
		UserMeetup userMeetup = getBasicUserMeetup(user);
		userMeetup.setStatus(UserMeetupState.PENDING);
		when(meetupRepository.findById(anyLong())).thenReturn(Optional.of(meetup));
		when(userMeetupRepository.findFirstByUserAndMeetup(any(User.class),any(Meetup.class))).thenReturn(Optional.of(userMeetup));
		MeetupException exception = assertThrows(MeetupException.class, () -> {
			userMeetupService.checkin(user,1l);
		});
		String expectedMessage = "User meetup state no support checkin";
		String actualMessage = exception.getMessage();
		Assertions.assertTrue(actualMessage.equals(expectedMessage));
	}

	@Test
	void checkinErrorWrongState() {
		User user = getBasicUser();
		UserMeetup userMeetup = getBasicUserMeetup(user);
		userMeetup.setStatus(UserMeetupState.PENDING);
		when(meetupRepository.findById(anyLong())).thenReturn(Optional.of(meetup));
		when(userMeetupRepository.findFirstByUserAndMeetup(any(User.class),any(Meetup.class))).thenReturn(Optional.of(userMeetup));
		MeetupException exception = assertThrows(MeetupException.class, () -> {
			userMeetupService.checkin(user,1l);
		});
		String expectedMessage = "User meetup state no support checkin";
		String actualMessage = exception.getMessage();
		Assertions.assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void checkinSaveError() {
		User user = getBasicUser();
		UserMeetup userMeetup = getBasicUserMeetup(user);
		userMeetup.setStatus(UserMeetupState.ACTIVE);
		when(meetupRepository.findById(anyLong())).thenReturn(Optional.of(meetup));
		when(userMeetupRepository.save(any(UserMeetup.class))).thenThrow(getMockDataIntegrityException());
		when(userMeetupRepository.findFirstByUserAndMeetup(any(User.class),any(Meetup.class))).thenReturn(Optional.of(userMeetup));
		DataIntegrity exception = assertThrows(DataIntegrity.class, () -> {
			userMeetupService.checkin(user,1l);
		});
	}

	@Test
	void confirmRegistrationOk() {
		User user = getBasicUser();
		UserMeetup userMeetup = getBasicUserMeetup(user);
		userMeetup.setStatus(UserMeetupState.PENDING);
		when(userMeetupRepository.save(any(UserMeetup.class))).thenReturn(userMeetup);
		when(userMeetupRepository.findFirstByMeetupAndToken(any(Meetup.class),any(String.class))).thenReturn(Optional.of(userMeetup));
		UserMeetup um = userMeetupService.confirmRegistration(meetup.getId(),"token");
		Assertions.assertNotNull(um);
	}

	@Test
	void confirmRegistrationError() {
		User user = getBasicUser();
		UserMeetup userMeetup = getBasicUserMeetup(user);
		userMeetup.setStatus(UserMeetupState.PENDING);
		when(userMeetupRepository.save(any(UserMeetup.class))).thenThrow(getMockDataIntegrityException());
		when(userMeetupRepository.findFirstByMeetupAndToken(any(Meetup.class),any(String.class))).thenReturn(Optional.of(userMeetup));
		DataIntegrity exception = assertThrows(DataIntegrity.class, () -> {
			userMeetupService.confirmRegistration(meetup.getId(),"token");
		});
		String expectedMessage = "Error changing user meetup state";
		String actualMessage = exception.getMessage();
		Assertions.assertTrue(actualMessage.equals(expectedMessage));
	}

	@Test
	void confirmRegistrationState() {
		User user = getBasicUser();
		UserMeetup userMeetup = getBasicUserMeetup(user);
		when(userMeetupRepository.findFirstByMeetupAndToken(any(Meetup.class),any(String.class))).thenReturn(Optional.of(userMeetup));
		MeetupException exception = assertThrows(MeetupException.class, () -> {
			userMeetupService.confirmRegistration(meetup.getId(),"token");
		});
		String expectedMessage = "User meetup state no support confirmation";
		String actualMessage = exception.getMessage();
		Assertions.assertTrue(actualMessage.contains(expectedMessage));
	}


	@Test
	void getUserMeetups() {
		User user = getBasicUser();
		when(userMeetupRepository.findByMeetupAndNotified(any(Meetup.class),any(Boolean.class))).thenReturn(Arrays.asList(getBasicUserMeetup(user)));
		List<UserMeetup> list = userMeetupService.getUserMeetups(meetup);
		Assertions.assertNotNull(list);
	}

	@Test
	void notified() {
		User user = getBasicUser();
		UserMeetup userMeetup = getBasicUserMeetup(user);
		when(userMeetupRepository.save(any(UserMeetup.class))).thenReturn(userMeetup);
		userMeetupService.notified(userMeetup);;
	}


}
