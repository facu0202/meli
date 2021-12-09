package com.facuferro.meetup.repository;

import com.facuferro.meetup.domain.TemperatureBeer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TemperatureBeerRepository extends JpaRepository<TemperatureBeer, Long> {

}