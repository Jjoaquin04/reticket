package com.example.reticket.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.reticket.db.Event;
import com.example.reticket.db.Ticket;
import com.example.reticket.service.EventService;
import com.example.reticket.service.TicketService;

@RestController
public class BuyTicketController {

    @Autowired
    private EventService eventService;
    @Autowired
    private TicketService ticketService;

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
