package com.example.reticket.controller;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

import jakarta.transaction.Transactional;

import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/events")
public class GetFilteredEventsPageController {

    @Autowired
    private EventService eventService;


    @Transactional
    @GetMapping("")
    public ModelAndView getFilteredEvents(
        @RequestParam(required = false) String startDateTime,
        @RequestParam(required = false) String endDateTime,
        @RequestParam(required = false) String location,
        @RequestParam(required = false) Event.EventType eventType,
        @RequestParam(required = false) String searchString,
        Model model
    ){
        List<Event> filteredEvents = eventService.getAllEvents();
        
        if (searchString != null && !searchString.isEmpty()) {
            filteredEvents = filteredEvents.stream()
                .filter(event -> 
                    (event.getName() != null && event.getName().toLowerCase().contains(searchString.toLowerCase()))  || 
                    eventService.getEventsByLocation(searchString).contains(event) ||
                    eventService.getEventsByVenue(searchString).contains(event)
                )
                .collect(Collectors.toList());
        }
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        if (startDateTime != null && !startDateTime.isEmpty()) {
            LocalDateTime start = LocalDate.parse(startDateTime, formatter).atStartOfDay();
            filteredEvents = filteredEvents.stream()
                .filter(event -> {
                    LocalDateTime eventDate = event.getDate();
                    return (eventDate.isEqual(start) || eventDate.isAfter(start));
                })
                .collect(Collectors.toList());
            System.out.println("Filtered by start date: " + start);
        }
        
        if (endDateTime != null && !endDateTime.isEmpty()) {
            LocalDateTime end = LocalDate.parse(endDateTime, formatter).atTime(23, 59);
            filteredEvents = filteredEvents.stream()
                .filter(event -> {
                    LocalDateTime eventDate = event.getDate();
                    return (eventDate.isEqual(end) || eventDate.isBefore(end));
                })
                .collect(Collectors.toList());
            System.out.println("Filtered by end date: " + end);
        }

        if (location != null && !location.isEmpty()) {
            filteredEvents = filteredEvents.stream()
                .filter((event -> eventService.getEventsByLocation(location).contains(event) || eventService.getEventsByVenue(location).contains(event)))
                .collect(Collectors.toList());
        }
        if (eventType != null) {
            filteredEvents = filteredEvents.stream()
                .filter(event -> eventService.getEventsByType(eventType).contains(event))
                .collect(Collectors.toList());
        }
        model.addAttribute("events", filteredEvents);
        return new ModelAndView("eventsPage");
    }

    @GetMapping("/")
    public ModelAndView eventsPage(Model model) {
        List<Event> allEventos = eventService.getAllEvents();
        model.addAttribute("events",allEventos);
        return new ModelAndView("eventsPage");
    }
}
