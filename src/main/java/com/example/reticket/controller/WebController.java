package com.example.reticket.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import com.example.reticket.db.Event;
import com.example.reticket.service.EventService;


@RestController
public class WebController {

    @Autowired
    private EventService eventService;

    @GetMapping("/")
    public ModelAndView mainPage(Model model) {
        List<Event> eventos = eventService.getAllEvents();
        model.addAttribute("mainEvents",eventos);
        return new ModelAndView("mainPageTemplate");
    }
    @GetMapping("/events")
    public ModelAndView allEventsPage(Model model)    {
        List<Event> eventos = eventService.getAllEvents();
        model.addAttribute("events", eventos);
        return new ModelAndView("eventsTemplate");
    }
    @GetMapping("/concerts")
    public ModelAndView concertsPage(Model model) {
        List<Event> concertsEvents = eventService.getEventsByType(Event.EventType.CONCERT);
        model.addAttribute("events", concertsEvents);
        return new ModelAndView("eventsTemplate");
    }
    @GetMapping("/festivals")
    public ModelAndView festivalPage(Model model) {
        List<Event> festivalEvents = eventService.getEventsByType(Event.EventType.FESTIVAL);
        model.addAttribute("events",festivalEvents);
        return new ModelAndView("eventsTemplate");
    }
    @GetMapping("/sports")
    public ModelAndView sportsPage(Model model) {
        List<Event> sportsEvents = eventService.getEventsByType(Event.EventType.SPORTS);
        model.addAttribute("events", sportsEvents);
        return new ModelAndView("eventsTemplate");
    }
    @GetMapping("/theater")
    public ModelAndView theaterPage(Model model)  {
        List<Event> theaterEvents = eventService.getEventsByType(Event.EventType.THEATER);
        model.addAttribute("events", theaterEvents);
        return new ModelAndView("eventsTemplate");
    }
}
