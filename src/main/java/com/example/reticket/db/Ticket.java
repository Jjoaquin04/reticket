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

    // Constructor vacío necesario para JPA
    public Ticket() {
    }

    public Ticket(Event event) {
        this.event = event;
        
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
}
