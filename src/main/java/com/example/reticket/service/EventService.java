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

    
    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    public Optional<Event> getEventById(Long id) {
        return eventRepository.findById(id);
    }
    public Event getEventByName(String name) {
        return eventRepository.findByNameIgnoringCase(name);
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }
    
    public List<Event> getEventsByLocation(String location) {
        return eventRepository.findByLocationIgnoringCase(location);
    }
    public List<Event> getEventsByVenue(String venue)   {
        return eventRepository.findByVenueIgnoringCase(venue);
    }
    
    public List<Event> getEventsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return eventRepository.findByDateBetween(startDate, endDate);
    }
    
    public List<Event> getEventsByType(Event.EventType eventType) {
        return eventRepository.findByEventType(eventType);
    }

    public List<Event> getEventsByCreatorId(Long creatorId) {
        return eventRepository.findByCreatorId(creatorId);
    }

    public Event updateEvent(Event event) {
        return eventRepository.save(event);
    }

    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }
}
