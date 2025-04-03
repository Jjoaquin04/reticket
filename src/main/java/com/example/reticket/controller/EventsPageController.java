package com.example.reticket.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import com.example.reticket.db.Event;
import com.example.reticket.service.EventService;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/events")
public class EventsPageController {

    @Autowired
    private EventService eventService;

    @GetMapping("")
    public ModelAndView getFilteredEvents(
        @RequestParam(required = false) Event.EventType eventType,
        @RequestParam(required = false) String location,
        @RequestParam(required = false) LocalDateTime startDate,
        @RequestParam(required = false) LocalDateTime endDate,
        Model model) {

        List<Event> filteredEvents = eventService.getAllEvents();
        
        if (eventType != null) {
            filteredEvents = filteredEvents.stream()
                .filter(event -> event.getEventType() == eventType)
                .collect(Collectors.toList());
        }
        
        if (location != null && !location.isEmpty()) {
            filteredEvents = filteredEvents.stream()
                .filter(event -> 
                    event.getLocation().toLowerCase().contains(location.toLowerCase()) || 
                    event.getVenue().toLowerCase().contains(location.toLowerCase()))
                .collect(Collectors.toList());
        }
        
        if (startDate != null && endDate != null) {
            filteredEvents = filteredEvents.stream()
                .filter(event -> 
                    (event.getDate().isEqual(startDate) || event.getDate().isAfter(startDate)) &&
                    (event.getDate().isEqual(endDate) || event.getDate().isBefore(endDate)))
                .collect(Collectors.toList());
        } else if (startDate != null) {
            // Si solo se proporciona la fecha de inicio
            filteredEvents = filteredEvents.stream()
                .filter(event -> event.getDate().isEqual(startDate) || event.getDate().isAfter(startDate))
                .collect(Collectors.toList());
        } else if (endDate != null) {
            // Si solo se proporciona la fecha de fin
            filteredEvents = filteredEvents.stream()
                .filter(event -> event.getDate().isEqual(endDate) || event.getDate().isBefore(endDate))
                .collect(Collectors.toList());
        }
        
        model.addAttribute("events", filteredEvents);
        return new ModelAndView("eventsTemplate");
    }
    
    @GetMapping("/")
    public ModelAndView mainPage(Model model) {
        List<Event> allEventos = eventService.getAllEvents();
        model.addAttribute("events",allEventos);
        return new ModelAndView("eventsTemplate");
    }
}
