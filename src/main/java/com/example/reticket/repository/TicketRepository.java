package com.example.reticket.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.reticket.db.Event;
import com.example.reticket.db.Ticket;

public interface TicketRepository extends JpaRepository<Ticket,Long>{

    // Buscar tickets por evento
    List<Ticket> findByEvent(Event event);
}
