package com.example.reticket.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


import com.example.reticket.db.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
    
    Event findByName(String name);
    List<Event> findByLocation(String location);
    List<Event> findByDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<Event> findByEventType(Event.EventType eventType);
}