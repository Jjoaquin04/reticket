package com.example.reticket.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.example.reticket.repository.EventRepository;
import com.example.reticket.repository.TicketRepository;
import com.example.reticket.repository.UserRepository;

import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
@Profile("local")
public class DataBaseInicializer {
    
    @Autowired
    private EventRepository eventRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private TicketRepository ticketRepository;
    
    @PostConstruct
    public void initDatabase() {
        // Crear eventos de ejemplo
        Event quevedo = new Event(
            "Concierto Quevedo", 
            LocalDateTime.of(2024, 7, 15, 21, 0), 
            "Movistar Arena, Madrid", 
            "Ultimas entradas para Quevedo en el Movistar Arena", 
            "img/cantantes/Quevedo.webp",
            "Imagen de quevedo",
            Event.EventType.CONCERT
        );
        
        Event cocaColaFest = new Event(
            "Festival Coca-Cola Music", 
            LocalDateTime.of(2024, 8, 5, 17, 0), 
            "IFEMA, Madrid", 
            "Festival con los mejores artistas nacionales e internacionales", 
            "img/festivales/festivalCocaCola.webp",
            "Imagen del festival Coca-Cola",
            Event.EventType.FESTIVAL
        );
        
        Event realMadrid = new Event(
            "Real Madrid vs Barcelona", 
            LocalDateTime.of(2024, 10, 20, 21, 0), 
            "Santiago Bernabéu, Madrid", 
            "El clásico de la liga española", 
            "img/deportes/Real-Madrid.webp",
            "Imagen del Santiago Bernabéu",
            Event.EventType.SPORTS
        );
        
        Event teatro = new Event(
            "Hamlet", 
            LocalDateTime.of(2024, 9, 10, 20, 0), 
            "Teatro Español, Madrid", 
            "Representación de la obra clásica de Shakespeare", 
            "../img/teatros/teatroLara.webp",
            "Imagen del teatro Lara de Madrid",
            Event.EventType.THEATER
        );
        
        // Guardar eventos
        List<Event> events = Arrays.asList(quevedo, cocaColaFest, realMadrid, teatro);
        eventRepository.saveAll(events);
        
        // Crear usuarios
        User_ user1 = new User_("juan", "password123", "juan@example.com");
        User_ user2 = new User_("maria", "securepass", "maria@example.com");
        User_ user3 = new User_("admin", "admin123", "admin@reticket.com");
        
        userRepository.saveAll(Arrays.asList(user1, user2, user3));
        
        // Crear tickets
        Ticket ticket1 = new Ticket(quevedo, "A1", "");
        Ticket ticket2 = new Ticket(quevedo, "A2", user1.getId().toString());
        Ticket ticket3 = new Ticket(cocaColaFest, "VIP1", "");
        Ticket ticket4 = new Ticket(realMadrid, "Grada 4 - 56",user2.getId().toString());
        
        ticketRepository.saveAll(Arrays.asList(ticket1, ticket2, ticket3, ticket4));
    }
}
