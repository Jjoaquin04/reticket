package com.example.reticket.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.reticket.db.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAll();
    Optional<Event> findById(Long id);
    Event save(Event evento);
    void deleteById(Long id);
    
    // Changed to match the field name in Event class
    List<Event> findByLocation(String location);
    
    // Custom query to find events between two dates
    @Query("SELECT e FROM Event e WHERE e.date BETWEEN :startDate AND :endDate")
    List<Event> findByDateBetween(@Param("startDate") LocalDateTime startDate, 
                                 @Param("endDate") LocalDateTime endDate);
    
    // Added more useful methods
    List<Event> findByEventType(Event.EventType eventType);
    List<Event> findByNameContainingIgnoreCase(String name);
    List<Event> findByOrganizer(String organizer);
}