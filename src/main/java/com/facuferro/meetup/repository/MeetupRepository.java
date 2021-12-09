package com.facuferro.meetup.repository;


import com.facuferro.meetup.domain.Meetup;
import com.facuferro.meetup.domain.MeetupState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;


@Repository
public interface MeetupRepository extends JpaRepository<Meetup, Long> {

    @Query(value = "from Meetup t where date BETWEEN :startDate AND :endDate")
    List<Meetup> getAllBetweenDates(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    List<Meetup> findByDateBeforeAndStatusNot(Date startDate, MeetupState state);

}