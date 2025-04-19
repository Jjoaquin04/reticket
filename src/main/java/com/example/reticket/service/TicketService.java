package com.example.reticket.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.reticket.db.Event;
import com.example.reticket.db.Ticket;
import com.example.reticket.repository.TicketRepository;

import java.util.List;
import java.util.Optional;


@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    // Create
    public Ticket createTicket(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    // Read
    public Optional<Ticket> getTicketById(Long id) {
        return ticketRepository.findById(id);
    }

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    public List<Ticket> getTicketsByEvent(Event event) {
        return ticketRepository.findByEvent(event);
    }

    public List<Ticket> getTicketsByUserId(Long userId) {
        return ticketRepository.findByUser_Id(userId);
    }

    // Update
    public Ticket updateTicket(Ticket ticket) {
        return ticketRepository.save(ticket);
    }
    
    // Delete
    public void deleteTicket(Long id) {
        ticketRepository.deleteById(id);
    }

    public List<Ticket> saveAllTickets(List<Ticket> tickets) {
        return ticketRepository.saveAll(tickets);
    }
}
