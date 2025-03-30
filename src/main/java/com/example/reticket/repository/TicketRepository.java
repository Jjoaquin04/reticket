package com.example.reticket.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.reticket.db.Event;
import com.example.reticket.db.Ticket;

public interface TicketRepository extends JpaRepository<Ticket,Long>{
    
    // Buscar tickets por evento
    List<Ticket> findByEvent(Event event);
    
    // Buscar tickets por usuario
    List<Ticket> findByUserId(String userId);
    
    // Buscar tickets disponibles para un evento
    List<Ticket> findByEventAndStatus(Event event, String status);
    
    // Buscar un asiento específico en un evento
    Optional<Ticket> findByEventAndSeatNumber(Event event, String seatNumber);
    
    // Contar tickets disponibles para un evento
    long countByEventAndStatus(Event event, String status);
    
    // Verificar si un asiento ya está reservado
    boolean existsByEventAndSeatNumberAndStatus(Event event, String seatNumber, String status);
    
    // Buscar tickets por estado
    List<Ticket> findByStatus(String status);
    
    // Consulta personalizada para obtener tickets con información del evento
    @Query("SELECT t FROM Ticket t JOIN t.event e WHERE e.name LIKE %:eventName%")
    List<Ticket> findByEventNameContaining(@Param("eventName") String eventName);
}
