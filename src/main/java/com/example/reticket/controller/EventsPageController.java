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
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/events")
public class EventsPageController {

    @Autowired
    private EventService eventService;

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
        if (startDateTime != null && !startDateTime.isEmpty() && endDateTime != null && !endDateTime.isEmpty()) {
            try {
                LocalDateTime start = LocalDate.parse(startDateTime, formatter).atStartOfDay();
                LocalDateTime end = LocalDate.parse(endDateTime, formatter).atTime(23, 59);
                filteredEvents = filteredEvents.stream()
                    .filter(event -> eventService.getEventsByDateRange(start, end).contains(event))
                    .collect(Collectors.toList());
            } catch (Exception e) {
                System.out.println("Error al parsear fechas: " + e.getMessage());
            }
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
    public ModelAndView mainPage(Model model) {
        List<Event> allEventos = eventService.getAllEvents();
        model.addAttribute("events",allEventos);
        return new ModelAndView("eventsPage");
    }
}
