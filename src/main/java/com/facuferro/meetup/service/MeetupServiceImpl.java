package com.facuferro.meetup.service;

import com.facuferro.meetup.api.MeetupRequest;
import com.facuferro.meetup.domain.Meetup;
import com.facuferro.meetup.domain.MeetupState;
import com.facuferro.meetup.exception.DataIntegrity;
import com.facuferro.meetup.exception.MeetupException;
import com.facuferro.meetup.helper.BeersHelper;
import com.facuferro.meetup.provider.WeatherService;
import com.facuferro.meetup.repository.MeetupRepository;
import com.facuferro.meetup.util.MeetupConstant;
import com.facuferro.meetup.util.MeetupUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.SchedulerLock;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
@Validated
public class MeetupServiceImpl implements  MeetupService{


    private MeetupRepository meetupRepository;
    private WeatherService weatherService;
    private BeersHelper beersHelper;

    public Meetup findById(Long id) {
        log.info("Find  Meetup by Id {}", id);
        return this.meetupRepository.findById(id).orElseThrow(() -> new MeetupException(MeetupConstant.MEETUP_NOT_FOUND));
    }

    public int calculateBeer(Meetup meetup){
        log.info("Calculate Beer for request {}", meetup);
        long user = meetup.getUsersMeetup().stream().filter(u -> u.isActive()).count();
        double temp = getTemperature(meetup);
        return beersHelper.countBeerBoxs(user,temp);
    }

    @Retryable(value = {RuntimeException.class}, maxAttempts = 4, backoff = @Backoff(1000))
    public Double getTemperature(Meetup meetup) {
        log.info("Get temperature to meetup id{}",meetup.toString());
        return weatherService.getTemperature(meetup);
    }


    public List<Meetup> findAll() {
        log.info("FindAll meetups");
        return this.meetupRepository.findAll();
    }


    @Recover
    private Double recover(RuntimeException e, Meetup meetup){
        log.info("Recover default temperature");
        return MeetupConstant.TEMPERATURE_DEFAULT;
    }

    public Meetup create(MeetupRequest meetupRequest) {
        log.info("Creating new Meetup for request {}", meetupRequest.toString());
        Meetup newMeetup = generateMeetup(meetupRequest);
        try {
            return meetupRepository.save(newMeetup);
        } catch (DataIntegrityViolationException exc) {
            throw new DataIntegrity("Error creating Meetup", exc);
        }
    }

    private Meetup generateMeetup(MeetupRequest meetupRequest) {
        return Meetup.builder()
                .title(meetupRequest.getTitle())
                .description(meetupRequest.getDescription())
                .status(MeetupState.ACTIVE)
                .date(meetupRequest.getDate())
                .location(meetupRequest.getLocation())
                .limitDate(meetupRequest.getLimitDate())
                .notified(false)
                .duration(meetupRequest.getDuration()).build();
    }



    public void notified(Meetup meetup) {
        log.info("Notify  Meetup for request {}", meetup.toString());
        meetup.setNotified(true);
        try {
            meetupRepository.save(meetup);
        } catch (DataIntegrityViolationException exc) {
            throw new DataIntegrity("Error notication Meetup", exc);
        }
    }

    public void closed(Meetup u) {
        log.info("Closed  Meetup for request {}", u.toString());
        u.toClosed();
        try {
            meetupRepository.save(u);
        } catch (DataIntegrityViolationException exc) {
            log.error("Closed Meetup", exc);
        }
    }


    private List<Meetup> findToClosed() {
        log.info("Find to closed");
        Date meetupDAte = new Date();
        return this.meetupRepository.findByDateBeforeAndStatusNot(meetupDAte,MeetupState.CLOSED);
    }

    @Scheduled(cron = "0 0 0/1 * * ?")
    @SchedulerLock(name = "MeetupCloser", lockAtLeastForString = "PT5M", lockAtMostForString = "PT14M")
    public void closedMeetups(){
        log.info("Closed Meetups");
        this.findToClosed().forEach(m -> this.closed(m));
        log.info("Closed Meetups");
    }

    public List<Meetup> getMeetupsToNotify() {
        log.info("Get meetups to notify");
        Date firstDate = new Date();
        Date endDate = MeetupUtil.addHoursToJavaUtilDate(firstDate,6);
        return meetupRepository.getAllBetweenDates(firstDate,endDate);
    }

}
