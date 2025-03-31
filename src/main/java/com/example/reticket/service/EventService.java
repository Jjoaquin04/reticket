package com.example.reticket.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.reticket.db.Event;
import com.example.reticket.repository.EventRepository;

@Service
public class EventService {
    
    @Autowired
    private EventRepository eventRepository;

    //Guardar/Crear event
    public Event createEvent(Event event)  {
        return eventRepository.save(event);
    }
    //Leer event
    public Event
}
