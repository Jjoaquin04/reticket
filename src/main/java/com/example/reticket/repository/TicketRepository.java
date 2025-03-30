package com.example.reticket.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.reticket.db.Ticket;

public interface TicketRepository extends JpaRepository<Ticket,Long>{
    
}
