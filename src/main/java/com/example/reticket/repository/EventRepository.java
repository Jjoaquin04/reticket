package com.example.reticket.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.reticket.db.Event;
import java.time.LocalDateTime;


public interface EventRepository extends JpaRepository<Event,Long>{

    List<Event> findAll();  // Devuelve todos los eventos
    Optional<Event> findById(Long id);  // Busca un evento por ID
    Event save(Event evento);  // Guarda o actualiza un evento
    void deleteById(Long id);
    List<Event> findByUbicacion(String localidad);
    List<Event> findByDate(LocalDateTime fechaInicio, LocalDateTime fechaFinal);


  
}