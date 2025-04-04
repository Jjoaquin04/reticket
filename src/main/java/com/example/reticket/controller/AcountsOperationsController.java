package com.example.reticket.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.reticket.db.Event;
import com.example.reticket.db.Event.EventStatus;
import com.example.reticket.db.Event.EventType;
import com.example.reticket.db.User_;
import com.example.reticket.service.EventService;
import com.example.reticket.service.UserService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;



 

@RestController
@RequestMapping("/acounts")
public class AcountsOperationsController {
    
    @Autowired
    private EventService eventService;
    @Autowired
    private UserService userService;

    @PostMapping("/newEvent")
    public ResponseEntity<?> createEvent(
        @RequestParam(required = true) String name,
        @RequestParam(required = true) String dateTime,
        @RequestParam(required = true) String location,
        @RequestParam(required = true) String venue,
        @RequestParam(required = true) String description,
        @RequestParam(required = true) String imageUrl,
        @RequestParam(required = true) String imageAltText,
        @RequestParam(required = true) EventType eventType,
        @RequestParam(required = true) EventStatus eventStatus,
        @RequestParam(required = true) int availableTickets
    ){
        try{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime date = LocalDateTime.parse(dateTime, formatter);
            
            Event newEvent = new Event(
                name, 
                date, 
                location, 
                venue, 
                description, 
                imageUrl, 
                imageAltText, 
                eventType, 
                eventStatus, 
                availableTickets
            );
            
            Event existingEvent = eventService.getEventByName(name);
            if(existingEvent != null)   {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Event with this name already exists.");
            }

            Event savedEvent = eventService.createEvent(newEvent);

            return ResponseEntity.status(HttpStatus.CREATED).body(savedEvent);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error creating event: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable long id) {
        Optional<Event> deletedEvent = eventService.getEventById(id);
        if(deletedEvent.isPresent())    {
            eventService.deleteEvent(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateEvent(@PathVariable long id, @RequestBody Event newEvent) {
        Optional<Event> event = eventService.getEventById(id);
        if(event != null)   {
            newEvent.setId(id);
            eventService.updateEvent(newEvent);
            return ResponseEntity.ok(newEvent);
        }else{
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/")
    public String getCreateEventPage() {
        return "newEventPage";
    }
    
    @GetMapping("/myEvents")
    public ModelAndView getEvents(Model model) {
        List<Event> allEvents = eventService.getAllEvents();
        model.addAttribute("events", allEvents);
        return new ModelAndView("myEventsPage");
    }

    @GetMapping("/users")
    public ModelAndView getUsers(Model model) {
        List<User_> allUsers = userService.getAllUsers();
        model.addAttribute("users", allUsers);
        return new ModelAndView("allUsersPage");
    }
    
    

}
    
