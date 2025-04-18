package com.example.reticket.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.reticket.db.Event;
import com.example.reticket.db.Ticket;

public interface TicketRepository extends JpaRepository<Ticket,Long>{

    List<Ticket> findByEvent(Event event);

    List<Ticket> findByUser_Id(Long userId);
}
