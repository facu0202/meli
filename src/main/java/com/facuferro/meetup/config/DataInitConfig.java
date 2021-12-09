package com.facuferro.meetup.config;

import com.facuferro.meetup.api.LocationRequest;
import com.facuferro.meetup.api.MeetupRequest;
import com.facuferro.meetup.api.UserMeetupRequest;
import com.facuferro.meetup.api.UserRequest;
import com.facuferro.meetup.domain.*;
import com.facuferro.meetup.service.*;
import com.facuferro.meetup.util.MeetupUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

@Configuration
@Slf4j
public class DataInitConfig {

    public static final String MEETUP_1 = "Meetup 1";
    public static final String MEETUP_2 = "Meetup 2";
    public static final String MEETUP_3 = "Meetup 3";

    @Bean
    public CommandLineRunner loadMeetup(LocationService locationService, MeetupService meetupService, UserMeetupService userMeetupService, UserService userService, TemperatureBeerService temperatureBeerService ) {
        return args -> {
            Location laPlata = locationService.create(LocationRequest.builder()
                    .description("La plata")
                    .latitude(-34.92145d)
                    .longitude(-57.95453d)
                    .build());

            log.info("initializing sample data");
            Meetup meetup1 = meetupService.create(MeetupRequest.builder()
                    .title(MEETUP_1)
                    .description(MEETUP_1)
                    .location(laPlata)
                    .date(MeetupUtil.addHoursToJavaUtilDate(new Date(), 2))
                    .limitDate(MeetupUtil.addHoursToJavaUtilDate(new Date(), 1))
                    .build());


            Meetup meetup2 = meetupService.create(MeetupRequest.builder()
                    .title( MEETUP_2)
                    .description(MEETUP_2)
                    .location(laPlata)
                    .date(new Date())
                    .date(MeetupUtil.addHoursToJavaUtilDate(new Date(), 6))
                    .limitDate(MeetupUtil.addHoursToJavaUtilDate(new Date(), 1))
                    .build());




            User user1 = userService.create(UserRequest.builder()
                    .name("Facundo")
                    .surname("Ferro")
                    .email("facundoferro@gmail.com")
                    .password("facu")
                    .role(Role.ROLE_ADMIN)
                    .build(), UserState.ACTIVE);

            User user2 = userService.create(UserRequest.builder()
                    .name("Silvana")
                    .surname("Bonafina")
                    .email("silbonafina@gmail.com")
                    .password("sil")
                    .role(Role.ROLE_USER)
                    .build(),UserState.ACTIVE);

            User user3 = userService.create(UserRequest.builder()
                    .name("Lautaro")
                    .surname("Ferro")
                    .email("lautaroferro@gmail.com")
                    .password("lauti")
                    .role(Role.ROLE_USER)
                    .build(),UserState.ACTIVE);

            userMeetupService.registerInMeetup(UserMeetupRequest.builder()
                    .user(user1)
                    .meetup(meetup1)
                    .build());

            userMeetupService.registerInMeetup(UserMeetupRequest.builder()
                    .user(user1)
                    .meetup(meetup2)
                    .build());

            userMeetupService.registerInMeetup(UserMeetupRequest.builder()
                    .user(user2)
                    .meetup(meetup1)
                    .build());

            userMeetupService.registerInMeetup(UserMeetupRequest.builder()
                    .user(user2)
                    .meetup(meetup2)
                    .build());

            userMeetupService.registerInMeetup(UserMeetupRequest.builder()
                    .user(user3)
                    .meetup(meetup2)
                    .build());
            temperatureBeerService.create(new TemperatureBeer(null,null, 20d, 0.75d));
            temperatureBeerService.create(new TemperatureBeer(null,20d, 24d, 1d));
            temperatureBeerService.create(new TemperatureBeer(null,24d, null, 2d));

        };


    }


}
