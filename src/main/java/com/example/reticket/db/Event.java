package com.example.reticket.db;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public enum EventType{
        CONCERT,
        FESTIVAL,
        THEATER,
        SPORTS
    }

    @Enumerated(EnumType.STRING)
    private EventType eventType;

    private String name;
    private LocalDateTime date;
    private String location;
    private String description;
    private String imageURL;
    private String organizer;
    
    // Default constructor for JPA
    public Event() {
    }
    
    public Event(String name, LocalDateTime date, String location, String description, 
                 String imageURL, String organizer, EventType eventType) {
        this.name = name;
        this.date = date;
        this.location = location;
        this.description = description;
        this.imageURL = imageURL;
        this.organizer = organizer;
        this.eventType = eventType;
    }

    public Long getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public LocalDateTime getDate() {
        return date;
    }
    
    public void setDate(LocalDateTime date) {
        this.date = date;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
    
    public String getOrganizer() {
        return organizer;
    }
    
    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }
    
    public EventType getEventType() {
        return eventType;
    }
    
    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }
}
