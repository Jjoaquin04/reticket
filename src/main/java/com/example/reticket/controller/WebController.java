package com.example.reticket.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.reticket.db.Event;
import com.example.reticket.repository.EventRepository;
import com.example.reticket.service.EventService;
import com.example.reticket.service.TicketService;
import org.springframework.web.bind.annotation.RequestParam;




@Controller
public class WebController {
    
    @Autowired
    private EventService eventService;

    @GetMapping("/")
    public String index()   {
        return "redirect:/mainPage";
    }
    @GetMapping("/mainPage")
    public String mainPage(Model model) {
        List<Event> eventos = eventService.getAllEvents();
        model.addAttribute("eventos",eventos);
        return "mainPageTemplate";
    }
    
}
