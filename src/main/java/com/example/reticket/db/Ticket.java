package com.example.reticket.db;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Event event;
    
    private String seatNumber;
    private String userId; 

    // Constructor vacío necesario para JPA
    public Ticket() {
    }

    public Ticket(Event event, String seatNumber, String userId) {
        this.event = event;
        this.seatNumber = seatNumber;
        this.userId = userId;
    }
    
    // Getters y setters sin cambios
    public Long getId() {
        return id;
    }
    public Event getEvent() {
        return event;
    }
    public void setEvent(Event event) {
        this.event = event;
    }
    public String getSeatNumber() {
        return seatNumber;
    }
    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
}
