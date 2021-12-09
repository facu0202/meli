package com.facuferro.meetup.meetup.unit.util;

import com.facuferro.meetup.api.ConfirmRegistrationRequest;
import com.facuferro.meetup.api.LocationRequest;
import com.facuferro.meetup.api.MeetupRequest;
import com.facuferro.meetup.api.UserRequest;
import com.facuferro.meetup.domain.*;
import com.facuferro.meetup.helper.BeersHelper;
import com.facuferro.meetup.helper.BeersHelperImpl;
import com.facuferro.meetup.provider.WeatherService;
import com.facuferro.meetup.repository.LocationRepository;
import com.facuferro.meetup.repository.MeetupRepository;
import com.facuferro.meetup.repository.UserMeetupRepository;
import com.facuferro.meetup.repository.UserRepository;
import com.facuferro.meetup.security.PasswordUtils;
import com.facuferro.meetup.service.MeetupService;
import com.facuferro.meetup.service.TemperatureBeerService;
import com.facuferro.meetup.util.MeetupUtil;
import com.facuferro.meetup.validator.UserMeetupValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class MeetupAbstractTest {

    protected Meetup meetup = basicMeetup();

    protected MeetupService meetupService;
    @Mock
    protected MeetupRepository meetupRepository;
    @Mock
    protected UserRepository userRepository;
    @Mock
    protected UserMeetupRepository userMeetupRepository;
    @Mock
    protected WeatherService weatherService;

    @Mock
    protected LocationRepository locationRepository;
    @Mock
    protected TemperatureBeerService temperatureBeerService;

    protected UserMeetupValidator userMeetupValidator = new UserMeetupValidator();

    protected BeersHelper beersHelper=new BeersHelperImpl(temperatureBeerService);

    protected Location location = new Location(1l,"La plata",10d,10d);

    protected List<UserMeetup> getUserMeetup(Meetup meetup, int cant){
        List<UserMeetup> result = new ArrayList<>();
        for(int i=0;i<=cant;i++){
            result.add(new UserMeetup(1l,meetup,null,"token", UserMeetupState.ACTIVE,false));
        }
        return result;
    }
    protected Meetup basicMeetup(){
        Meetup newMeetup = new Meetup();
        newMeetup.setLocation(location);
        newMeetup.setUsersMeetup(new ArrayList<>());
        newMeetup.setDate(new Date());
        newMeetup.setDescription("Test");
        newMeetup.setDuration(1);
        newMeetup.setId(1l);
        newMeetup.setLimitDate(new Date());
        newMeetup.setStatus(MeetupState.ACTIVE);
        newMeetup.setTitle("test");
        return newMeetup;
    }

    protected List<TemperatureBeer> getTemperatures() {
        List<TemperatureBeer>  temperatureBeers = new ArrayList();
        temperatureBeers.add(new TemperatureBeer(null,null, 20d, 0.75d));
        temperatureBeers.add(new TemperatureBeer(null,20d, 24d, 1d));
        temperatureBeers.add(new TemperatureBeer(null,24d, null, 2d));
        return temperatureBeers;
    }

    protected TemperatureBeer getBasicTemperature() {
        return (new TemperatureBeer(null, null, 20d, 0.75d));
    }

    protected ConfirmRegistrationRequest getBasicConfirmRegistrationRequest(long meetupId, String token){
        return  ConfirmRegistrationRequest.builder().user(getBasicUser()).
                token(token)
                .meetup(getBasicMeetup(meetupId)).build();
    }
    protected User getBasicUser(){
        return User.builder()
                .id(1l)
                .name("Facundo")
                .surname("Ferro")
                .email("facundoferro@gmail.com")
                .pwd(PasswordUtils.encrypt("password"))
                .role(Role.ROLE_ADMIN)
                .status(UserState.ACTIVE)
                .token("token")
                .build();
    }

    protected UserRequest getBasicUserRequest(){
        return UserRequest.builder()
                .name("Facundo")
                .surname("Ferro")
                .email("facundoferro@gmail.com")
                .password("facu")
                .role(Role.ROLE_ADMIN)
                .build();
    }



    protected Meetup getBasicMeetup(long i){
        return Meetup.builder()
                .id(i)
                .title("Meetup "+1)
                .date(MeetupUtil.addHoursToJavaUtilDate(new Date(),4))
                .description("Description "+i)
                .limitDate(MeetupUtil.addHoursToJavaUtilDate(new Date(),1))
                 .duration(1)
                .status(MeetupState.ACTIVE)
                .location(location)
                .notified(false)
                .build();
    }

    protected LocationRequest getBasicLocationRequest() {
        return LocationRequest.builder()
                .description("La plata")
                .latitude(-34.92145d)
                .longitude(-57.95453d)
                .build();
    }
    protected MeetupRequest getBasicMeetupRequest(){
        return MeetupRequest.builder()
            .title("Meetup 1")
				.description("Meetup 1")
				.location(location)
				.date(new Date())
            .limitDate(new Date())
            .build();
    }

    protected UserMeetup getBasicUserMeetup(User user) {
        return UserMeetup.builder()
                .user(user)
                .meetup(meetup)
                .status(UserMeetupState.ACTIVE)
                .notified(false)
                .token(UUID.randomUUID().toString())
                .build();
    }

    protected DataIntegrityViolationException getMockDataIntegrityException() {
        return new DataIntegrityViolationException("Mock error");
    }


    public  String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
