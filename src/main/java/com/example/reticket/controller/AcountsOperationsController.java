package com.example.reticket.controller;

import org.springframework.web.bind.annotation.RestController;
import com.example.reticket.db.Event;
import com.example.reticket.db.Event.EventStatus;
import com.example.reticket.db.Event.EventType;
import com.example.reticket.db.User_;
import com.example.reticket.service.EventService;
import com.example.reticket.service.UserService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import org.springframework.web.bind.annotation.RequestParam;



 

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
        Optional<Event> existingEvent = eventService.getEventById(event.getId());

        if(existingEvent.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error","El evento ya existe"));
        }

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
    @PatchMapping("/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id,@RequestBody Map<String, Object> updates) {
        Optional<User_> existingUser = userService.getUserById(id);
        if (!existingUser.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        User_ user = existingUser.get();
        
        // Actualizar solo los campos proporcionados en la solicitud
        if (updates.containsKey("username")) {
            String newUsername = (String) updates.get("username");
            
            // Verificar que el nuevo username no exista ya
            if (!user.getUsername().equals(newUsername) && userService.existsByUsername(newUsername)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error","El nombre de usuario ya está en uso"));
            }
            user.setUsername(newUsername);
        }
        
        if (updates.containsKey("email")) {
            String newEmail = (String) updates.get("email");
            
            // Verificar que el nuevo email no exista ya
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

        // Dont return the password
        Map<String, Object> response = Map.of(
            "username", updatedUser.getUsername(),
            "email", updatedUser.getEmail(),
            "password",updatedUser.getPassword()
        );
        
        return ResponseEntity.ok(response);
    }

}
    
