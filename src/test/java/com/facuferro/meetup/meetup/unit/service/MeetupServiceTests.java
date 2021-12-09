package com.facuferro.meetup.meetup.unit.service;

import com.facuferro.meetup.domain.Location;
import com.facuferro.meetup.domain.Meetup;
import com.facuferro.meetup.domain.MeetupState;
import com.facuferro.meetup.helper.BeersHelperImpl;
import com.facuferro.meetup.exception.DataIntegrity;
import com.facuferro.meetup.exception.MeetupException;
import com.facuferro.meetup.meetup.unit.util.MeetupAbstractTest;
import com.facuferro.meetup.service.MeetupServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MeetupServiceTests extends MeetupAbstractTest {

	@BeforeEach
	public void setup() {
		meetupService = new MeetupServiceImpl(meetupRepository, weatherService,new BeersHelperImpl(temperatureBeerService));
		meetup = getBasicMeetup(1l);
		meetup.setUsersMeetup(getUserMeetup(meetup,10));
	}

	@Test
	void findOk() {
		when(meetupRepository.findById(anyLong())).thenReturn(Optional.of(meetup));
		Meetup findMeetup = meetupService.findById(123l);
		Assertions.assertNotNull(findMeetup);
	}

	@Test
	void findNotOk() {
		when(meetupRepository.findById(anyLong())).thenReturn(Optional.empty());
		Assertions.assertThrows(MeetupException.class,()->meetupService.findById(123l));
	}


	@Test
	void countBeerBoxesCold() {
		when(temperatureBeerService.findAll()).thenReturn(getTemperatures());
		when(weatherService.getTemperature(any(Meetup.class))).thenReturn(10d);
		int countBeerBoxes = meetupService.calculateBeer(meetup);
		Assertions.assertEquals(2,countBeerBoxes);
	}

	@Test
	void countBeerBoxesLimit() {
		when(temperatureBeerService.findAll()).thenReturn(getTemperatures());
		when(weatherService.getTemperature(any(Meetup.class))).thenReturn(20d);
		int countBeerBoxes = meetupService.calculateBeer(meetup);
		Assertions.assertEquals(2,countBeerBoxes);
	}

	@Test
	void countBeerBoxes99UserAndCold() {
		when(temperatureBeerService.findAll()).thenReturn(getTemperatures());
		when(weatherService.getTemperature(any(Meetup.class))).thenReturn(10d);
		Meetup meetup115 = getBasicMeetup(115);
		meetup115.setUsersMeetup(getUserMeetup(meetup115,115));
		int countBeerBoxes = meetupService.calculateBeer(meetup115);
		Assertions.assertEquals(20,countBeerBoxes);
	}

	@Test
	void countBeerBoxes99UserAndHot() {
		when(temperatureBeerService.findAll()).thenReturn(getTemperatures());
		when(weatherService.getTemperature(any(Meetup.class))).thenReturn(26d);
		Meetup meetup115 = getBasicMeetup(115);
		meetup115.setUsersMeetup(getUserMeetup(meetup115,115));
		int countBeerBoxes = meetupService.calculateBeer(meetup115);
		Assertions.assertEquals(39,countBeerBoxes);
	}
	@Test
	void countBeerBoxesHot() {
		when(temperatureBeerService.findAll()).thenReturn(getTemperatures());
		when(weatherService.getTemperature(any(Meetup.class))).thenReturn(26d);
		int countBeerBoxes = meetupService.calculateBeer(meetup);
		Assertions.assertEquals(4,countBeerBoxes);
	}
	@Test
	void countBeerBoxesHotWithTemperatureError() {
		when(temperatureBeerService.findAll()).thenReturn(new ArrayList<>());
		when(weatherService.getTemperature(any(Meetup.class))).thenReturn(26d);
		Assertions.assertThrows(MeetupException.class,()->
		meetupService.calculateBeer(meetup));
	}

	@Test
	void findAll() {
		when(meetupRepository.findAll()).thenReturn(Arrays.asList(meetup));
		List<Meetup> list = meetupService.findAll();
		Assertions.assertEquals(1,list.size());
	}

	@Test
	void createMeetup() {

		Location laPlata = new Location();
		laPlata.setId(1l);
		when(meetupRepository.save(any(Meetup.class))).thenReturn(meetup);
		Meetup meetup1 = meetupService.create(getBasicMeetupRequest());

		Assertions.assertNotNull(meetup1);
	}

	@Test
	void notifyMeetup() {
		when(meetupRepository.save(any(Meetup.class))).thenReturn(meetup);
		meetupService.notified(meetup);
	}

	@Test
	void notifyMeetupError() {
		when(meetupRepository.save(any(Meetup.class))).thenThrow(super.getMockDataIntegrityException());
		DataIntegrity exception = assertThrows(DataIntegrity.class, () -> {
			meetupService.notified(meetup);
		});

		String expectedMessage = "Error notication Meetup";
		String actualMessage = exception.getMessage();
		Assertions.assertTrue(actualMessage.contains(expectedMessage));

	}

	@Test
	void createMeetupError() {
		DataIntegrity exception = assertThrows(DataIntegrity.class, () -> {
		when(meetupRepository.save(any(Meetup.class))).thenThrow(super.getMockDataIntegrityException());
		Meetup meetup1 = meetupService.create(getBasicMeetupRequest());
		});
		String expectedMessage = "Error creating Meetup";
		String actualMessage = exception.getMessage();
		Assertions.assertTrue(actualMessage.contains(expectedMessage));
	}


	@Test
	void closed() {
		when(meetupRepository.findByDateBeforeAndStatusNot(any(Date.class),any(MeetupState.class))).thenReturn(Arrays.asList(meetup));
		when(meetupRepository.save(any(Meetup.class))).thenThrow(super.getMockDataIntegrityException());
		meetupService.closedMeetups();
	}

	@Test
	void closedError() {
		when(meetupRepository.findByDateBeforeAndStatusNot(any(Date.class),any(MeetupState.class))).thenReturn(Arrays.asList(meetup));
		when(meetupRepository.save(any(Meetup.class))).thenReturn(meetup);
		meetupService.closedMeetups();
	}

	@Test
	void getUserMeetups() {
		when(meetupRepository.findByDateBeforeAndStatusNot(any(Date.class),any(MeetupState.class))).thenReturn(Arrays.asList(meetup));
		when(meetupRepository.save(any(Meetup.class))).thenReturn(meetup);
		meetupService.closedMeetups();
	}

	@Test
	void getMeetupsToNotify() {
		when(meetupRepository.getAllBetweenDates(any(Date.class),any(Date.class))).thenReturn(Arrays.asList(meetup));
		meetupService.getMeetupsToNotify();
	}


}
