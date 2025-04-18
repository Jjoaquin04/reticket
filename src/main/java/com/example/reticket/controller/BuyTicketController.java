package com.example.reticket.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.reticket.db.Event;
import com.example.reticket.db.Ticket;
import com.example.reticket.db.User_;
import com.example.reticket.service.EventService;
import com.example.reticket.service.TicketService;
import com.example.reticket.service.UserService;

import jakarta.servlet.http.HttpSession;

@RestController
public class BuyTicketController {

    @Autowired
    private EventService eventService;
    @Autowired
    private TicketService ticketService;
    @Autowired
    private UserService userService;

    @PostMapping("buyTicket/{id}")
    public ResponseEntity<?> buyTicket(@PathVariable Long id, HttpSession session) {
                Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no autenticado");
        }
        
                Optional<Event> existingEvent = eventService.getEventById(id);
        if(!existingEvent.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Event event = existingEvent.get();
        
    
        if (event.getCurrenNumberOfTickets() <= 0) {
            return ResponseEntity.badRequest().body("No hay tickets disponibles");
        }
        
        Optional<User_> userOpt = userService.getUserById(userId);
        if (!userOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no encontrado");
        }
        User_ user = userOpt.get();
        
        Ticket ticket = new Ticket(event);
        user.addTicket(ticket);
        
        ticketService.createTicket(ticket);
        
        event.setCurrenNumberOfTickets(event.getCurrenNumberOfTickets() - 1);
        if(event.getCurrenNumberOfTickets() == 0) {
            event.setEventStatus(Event.EventStatus.SOLD_OUT);
        }
        eventService.updateEvent(event);

        return ResponseEntity.ok(ticket);
    }
}
