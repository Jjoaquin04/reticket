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
    
    @ManyToOne
    private User_ user;

    public Ticket() {
    }

    public Ticket(Event event,User_ user) {
        this.event = event;
        this.user = user;
    }
    
    // Getters y setters
    public Long getId() {
        return id;
    }
    
    public Event getEvent() {
        return event;
    }
    
    public void setEvent(Event event) {
        this.event = event;
    }
    
    public User_ getUser() {
        return user;
    }
    
    public void setUser(User_ user) {
        this.user = user;
    }
}
