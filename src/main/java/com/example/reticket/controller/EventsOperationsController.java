package com.example.reticket.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.reticket.db.Event;
import com.example.reticket.service.EventService;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class EventsOperationsController {
    
    @Autowired
    private EventService eventService;

    @PostMapping("")
    public ResponseEntity<?> postNewEvent(@RequestBody Event event) {
        Optional<Event> existingEvent = eventService.getEventById(event.getId());
        if(existingEvent.isPresent())   {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Ya existe un evento con esta información");
        }
    }
    
}
