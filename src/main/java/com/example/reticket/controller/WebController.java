package com.example.reticket.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


import com.example.reticket.db.Event;
import com.example.reticket.service.EventService;


@Controller
public class WebController {
    
    @Autowired
    private EventService eventService;

    @GetMapping("/")
    public String mainPage(Model model) {
        List<Event> eventos = eventService.getAllEvents();
        model.addAttribute("mainEvents",eventos);
        return "mainPageTemplate";
    }
    @GetMapping("/events")
    public String allEventsPage(Model model)    {
        List<Event> eventos = eventService.getAllEvents();
        model.addAttribute("events", eventos);
        return "allEventsTemplate";
    }
    @GetMapping("/concerts")
    public String concertsPage(Model model) {
        List<Event> concertsEvents = eventService.getEventsByType(Event.EventType.CONCERT);
        model.addAttribute("concerts", concertsEvents);
        return "concertsTemplate";
    }
    @GetMapping("/festivals")
    public String festivalPage(Model model) {
        List<Event> festivalEvents = eventService.getEventsByType(Event.EventType.FESTIVAL);
        model.addAttribute("festival",festivalEvents);
        return "festivalTemplate";
    }
    @GetMapping("/sports")
    public String sportsPage(Model model) {
        List<Event> sportsEvents = eventService.getEventsByType(Event.EventType.SPORTS);
        model.addAttribute("sports", sportsEvents);
        return "sportsTemplate";
    }
    @GetMapping("/theater")
    public String theaterPage(Model model)  {
        List<Event> theaterEvents = eventService.getEventsByType(Event.EventType.THEATER);
        model.addAttribute("theaters", theaterEvents);
        return "theaterTemplate";
    }
}
