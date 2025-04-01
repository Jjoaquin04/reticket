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
        model.addAttribute("eventos",eventos);
        return "mainPageTemplate";
    }
    @GetMapping("/concerts")
    public String concertsPage(Model model) {
        List<Event> concerEvents = eventService.getEventsByType(Event.EventType.CONCERT);
        model.addAttribute("conciertos", concerEvents);
        return "concertsTemplate";
    }
    
}
