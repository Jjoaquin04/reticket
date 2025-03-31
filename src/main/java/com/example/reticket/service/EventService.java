package com.example.reticket.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.reticket.db.Event;
import com.example.reticket.repository.EventRepository;

@Service
public class EventService {
    
    @Autowired
    private EventRepository eventRepository;

    // Create
    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    // Read
    public Optional<Event> getEventById(Long id) {
        return eventRepository.findById(id);
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }
    
    public List<Event> getEventsByLocation(String location) {
        return eventRepository.findByLocation(location);
    }
    
    public List<Event> getEventsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return eventRepository.findByDateBetween(startDate, endDate);
    }
    
    public List<Event> getEventsByType(Event.EventType eventType) {
        return eventRepository.findByEventType(eventType);
    }
    
    public List<Event> searchEventsByName(String keyword) {
        return eventRepository.findByNameContainingIgnoreCase(keyword);
    }

    // Update
    public Event updateEvent(Event event) {
        return eventRepository.save(event);
    }

    // Delete
    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }
}
