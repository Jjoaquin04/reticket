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
    public enum EventStatus{
        AVAILABLE,
        CANCELLED,
        FINISHED,
        SOLD_OUT
    }

    @Enumerated(EnumType.STRING)
    private EventType eventType;
    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus;

    private String name;
    private LocalDateTime date;
    private String venue;
    private String location;
    private String description;
    private String imageURL;
    private String altImage;
    private int currenNumberOfTickets;
    

    public Event() {
    }
    
    public Event(String name, LocalDateTime date,String venue, String location, String description, 
                 String imageURL,String altImage,EventType eventType,EventStatus eventStatus,int currenNumberOfTickets) {
        this.name = name;
        this.date = date;
        this.venue = venue;
        this.location = location;
        this.description = description;
        this.imageURL = imageURL;
        this.altImage = altImage;
        this.eventType = eventType;
        this.eventStatus = eventStatus;
        this.currenNumberOfTickets = currenNumberOfTickets;
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

    public void setVenue(String venue) {
        this.venue = venue;
    }
    public String getVenue() {
        return venue;
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
    public void setAltImage(String altImage) {
        this.altImage = altImage;
    }
    public String getAltImage() {
        return altImage;
    }
    
    public EventType getEventType() {
        return eventType;
    }
    
    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }
    public int getCurrenNumberOfTickets() {
        return currenNumberOfTickets;
    }
    public EventStatus getEventStatus() {
        return eventStatus;
    }
    public void setCurrenNumberOfTickets(int currenNumberOfTickets) {
        this.currenNumberOfTickets = currenNumberOfTickets;
    }
    public void setEventStatus(EventStatus eventStatus) {
        this.eventStatus = eventStatus;
    }
}
