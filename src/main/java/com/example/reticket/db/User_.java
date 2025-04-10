package com.example.reticket.db;


import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;

@Entity
public class User_{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;
    private String password;
    private String email;
    
    @OneToMany(cascade = CascadeType.ALL)
    private List<Ticket> tickets = new ArrayList<>();

    public User_() {
    }

    public User_(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
    public Long getId() {
        return id;
    }
    public String getUsername() {
        return this.username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public List<Ticket> getTickets() {
        return tickets;
    }
    public void addTicket(Ticket ticket)    {
        if(!tickets.add(ticket))    {
            throw new RuntimeException("Error al guardar el ticket");
        }
    }
    public User_ get(){
        return this;
    }
    
}
