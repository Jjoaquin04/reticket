package com.example.reticket.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.reticket.service.EventService;
import com.example.reticket.db.Event;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
public class MainPageController {
    
    @Autowired
    private EventService eventService;

    @GetMapping("/")
    public ModelAndView mainPage(Model model)    {
        List<Event> eventos = eventService.getAllEvents();
        model.addAttribute("mainEvents", eventos);
        return new ModelAndView("mainPage");
    }
}
