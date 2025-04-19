package com.example.reticket.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.reticket.db.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    Event findByNameIgnoringCase(String name);
    List<Event> findByLocationIgnoringCase(String location);
    List<Event> findByVenueIgnoringCase(String venue);
    List<Event> findByDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<Event> findByEventType(Event.EventType eventType);
    List<Event> findByCreatorId(Long creatorId);
}