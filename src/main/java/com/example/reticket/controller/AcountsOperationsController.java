package com.example.reticket.controller;

import org.springframework.web.bind.annotation.RestController;
import com.example.reticket.db.Event;
import com.example.reticket.db.User_;
import com.example.reticket.service.EventService;
import com.example.reticket.service.UserService;

import java.util.Map;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;




 

@RestController
public class AcountsOperationsController {
    
    @Autowired
    private EventService eventService;
    @Autowired
    private UserService userService;

    @PostMapping("/submitEvent")
    public ResponseEntity<?> createEvent(
        @RequestBody(required = true) Event event
    ){
        Event newEvent = eventService.createEvent(event);
        return ResponseEntity.status(HttpStatus.CREATED).body(newEvent);
    }

    @DeleteMapping("events/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable Long id) {
        Optional<Event> deletedEvent = eventService.getEventById(id);
        if(deletedEvent.isPresent())    {
            eventService.deleteEvent(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
    @PutMapping("events/{id}")
    public ResponseEntity<?> updateEvent(@PathVariable Long id, @RequestBody Event newEvent) {
        Optional<Event> event = eventService.getEventById(id);
        if(event != null)   {
            newEvent.setId(id);
            eventService.updateEvent(newEvent);
            return ResponseEntity.ok(newEvent);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("events/{id}")
    public ResponseEntity<?> updateEventStatus(@PathVariable Long id, @RequestBody Event.EventStatus status) {
        Optional<Event> event = eventService.getEventById(id);
        if(event.isPresent()){
            Event updatedEvent = event.get();
            updatedEvent.setEventStatus(status);
            eventService.updateEvent(updatedEvent);
            return ResponseEntity.ok(updatedEvent);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id,@RequestBody Map<String, Object> updates) {
        Optional<User_> existingUser = userService.getUserById(id);
        if (!existingUser.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        User_ user = existingUser.get();
        
        // Update only the fields provided in the request
        if (updates.containsKey("username")) {
            String newUsername = (String) updates.get("username");
            
            // Verify that the new username doesn't already exist
            if (!user.getUsername().equals(newUsername) && userService.existsByUsername(newUsername)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error","El nombre de usuario ya está en uso"));
            }
            user.setUsername(newUsername);
        }
        
        if (updates.containsKey("email")) {
            String newEmail = (String) updates.get("email");
            
            // Verify that the new email doesn't already exist
            if (!user.getEmail().equals(newEmail) && userService.existsByEmail(newEmail)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error","El email ya está en uso"));
            }
            user.setEmail(newEmail);
        }
        
        if (updates.containsKey("password")) {
            if(user.getPassword().equals(updates.get("password"))) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error","La contraseña no puede ser la misma que la anterior"));
            }
            user.setPassword((String) updates.get("password"));
        }
        
        User_ updatedUser = userService.updateUser(user);

        // Don't return the password
        Map<String, Object> response = Map.of(
            "username", updatedUser.getUsername(),
            "email", updatedUser.getEmail(),
            "password",updatedUser.getPassword()
        );
        
        return ResponseEntity.ok(response);
    }

}

