package com.facuferro.meetup.controller;


import com.facuferro.meetup.api.*;
import com.facuferro.meetup.domain.Meetup;
import com.facuferro.meetup.domain.User;
import com.facuferro.meetup.domain.UserMeetup;
import com.facuferro.meetup.service.MeetupService;
import com.facuferro.meetup.service.NotificationService;
import com.facuferro.meetup.service.UserMeetupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@Slf4j
@RequestMapping(value="meetups")
@Api(description = "Endpoint meetups y sus participantes.",tags = {"meetup"})
public class MeetupController {

    private MeetupService meetupService;
    private UserMeetupService userMeetUpService;
    private NotificationService notificationService;


    public MeetupController(MeetupService meetupService,
                            UserMeetupService userMeetUpService,
                            NotificationService notificationService) {
        this.meetupService = meetupService;
        this.userMeetUpService = userMeetUpService;
        this.notificationService = notificationService;
    }


    @ApiOperation(value = "Calcular cerveza", notes = "Calcula la cantidad de ceverza necesaria para una meetup. Uso exclusivo para aministradores.", tags = { "meetup","beer" })
    @GetMapping(value = "/{id}/beers/calculate")
    public ResponseEntity<MeetupBeerResponse> beers(@PathVariable("id") Long meetupId) {
         log.info("Calculate beers for meetup id {}",meetupId);
         return ResponseEntity.ok(MeetupBeerResponse.createFrom(meetupService.calculateBeer(meetupService.findById(meetupId))));
    }

    @GetMapping(value = "/{id}/temperature")
    @ApiOperation(value = "Informar temperatura", notes = "Informa la temperatura para una meetup", tags = { "meetup","temperatura" })
    public ResponseEntity<MeetupResponse.MeetupTemperatureResponse> temperature(@PathVariable("id") Long meetupId) {
        log.info("ÇGet temperature for meetup id {}",meetupId);
        return ResponseEntity.ok(MeetupResponse.MeetupTemperatureResponse.createFrom(meetupService.getTemperature(meetupService.findById(meetupId))));
    }

    @GetMapping()
    @ApiOperation(value = "Lista las meetup", notes = "Lista todas las meetups. Uso exclusivo para aministradores.", tags = { "meetup" })
    public ResponseEntity<List<MeetupResponse>> findAll() {
        List<MeetupResponse> responseList = meetupService.findAll().stream().map(MeetupResponse::createFrom).collect(Collectors.toList());
        log.info("List all meetups");
        return ResponseEntity.ok(responseList);
    }

    @GetMapping(value = "/{id}")
    @ApiOperation(value = "Consulta de meetup", notes = "Muestra la  informacion de un meetup. Uso exclusivo para aministradores.", tags = { "user" })
    public ResponseEntity<MeetupResponse> find(@PathVariable("id") Long meetupId) {
        log.info("Find meetup for id {}",meetupId);
        return ResponseEntity.ok(MeetupResponse.createFrom(meetupService.findById(meetupId)));
    }

    @PostMapping
    @ApiOperation(value = "Crea una meetup", notes = "Crea una meetup. Uso exclusivo para aministradores.", tags = { "meetup" })
    public ResponseEntity<MeetupResponse> createMeetup(@RequestBody @Valid MeetupRequest meetupRequest)
    {
        log.info("Create meetup for request {}",meetupRequest);
        Meetup meetupResult = meetupService.create(meetupRequest);
        return ResponseEntity.ok(MeetupResponse.createFrom(meetupResult));
    }

    @PostMapping(value = "/{id}/register")
    @ApiOperation(value = "Registrar usuario en una meetup", notes = "Registra un usuario en una meetup.", tags = { "meetup","user" })
    public ResponseEntity<UserMeetupResponse> registerInMeeting(@PathVariable("id") Long meetupId, @ApiIgnore @AuthenticationPrincipal User user)
    {
        log.info("Register user to  meetup for request {}",meetupId);
        UserMeetup userMeetup = userMeetUpService.registerInMeeting(user,meetupId);
        notificationService.notifyInscription(userMeetup);
        return ResponseEntity.ok(UserMeetupResponse.createFrom(userMeetup));

    }

    @GetMapping(value = "/{id}/confirm/{token}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Confirma un usuario que se registro en una meetup", notes = "Confirma un usuario que se registro en una meetup. Uso exclusivo para aministradores.", tags = { "meetup" })
    @ApiParam(value="user", hidden=true)
    public void confirmRegistration(@PathVariable("id") Long meetupId,@PathVariable("token") String token)
    {
        log.info("Confirm registration for id {}",meetupId);
       userMeetUpService.confirmRegistration(meetupId,token);
    }

    @PostMapping(value = "/{id}/checkin")
    @ApiOperation(value = "Checkin de usuario en una meetup", notes = "Realiza el checkin de  un usuario en una meetup.", tags = { "meetup","user" })
    @ResponseStatus(HttpStatus.OK)
    public void checkin(@PathVariable("id") Long meetupId, @ApiIgnore @AuthenticationPrincipal User user, @RequestBody @Valid ConfirmRegistrationRequest confirmRegistrationRequest)
    {
        log.info("Checkin for id {}",meetupId);
        userMeetUpService.checkin(user,meetupId);
    }

}
