package com.example.reticket.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.reticket.service.EventService;
import com.example.reticket.service.TicketService;
import com.example.reticket.db.Event;
import com.example.reticket.db.Ticket;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
public class MainPageController {
    
    @Autowired
    private EventService eventService;
    @Autowired
    private TicketService ticketService;

    @GetMapping("/")
    public ModelAndView mainPage(Model model)    {
        List<Event> eventos = eventService.getAllEvents();
        model.addAttribute("mainEvents", eventos);
        return new ModelAndView("mainPage");
    }

    @PostMapping("buyTicket/{id}")
    public ResponseEntity<?> buyTicket(@PathVariable Long id) {
        Optional<Event> existingEvent = eventService.getEventById(id);
        if(!existingEvent.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Event event = existingEvent.get();

        Ticket ticket = new Ticket(event);
        ticketService.createTicket(ticket);
        
        event.setCurrenNumberOfTickets(event.getCurrenNumberOfTickets() - 1);
        if(event.getCurrenNumberOfTickets() == 0) {
            event.setEventStatus(Event.EventStatus.SOLD_OUT);
        }

        eventService.updateEvent(event);

        return ResponseEntity.ok(ticket);
    }
}
