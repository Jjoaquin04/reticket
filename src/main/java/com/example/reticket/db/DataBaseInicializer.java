package com.example.reticket.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.example.reticket.repository.EventRepository;
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
    
    
    
    @PostConstruct
    public void initDatabase() {
        // Create events examples
        Event quevedo = new Event(
            "Concierto Quevedo", 
            LocalDateTime.of(2025, 7, 15, 21, 0),
            "Movistar Arena",
            "Madrid", 
            "Ultimas entradas para Quevedo en el Movistar Arena", 
            "/img/cantantes/Quevedo.webp",
            "Imagen de quevedo",
            Event.EventType.CONCERT,
            Event.EventStatus.AVAILABLE,
            200
        );
        
        Event cocaColaFest = new Event(
            "Festival Coca-Cola Music", 
            LocalDateTime.of(2025, 8, 5, 17, 0),
            "IFEMA",
            "Madrid", 
            "Festival con los mejores artistas nacionales e internacionales", 
            "/img/festivales/festivalCocaCola.webp",
            "Imagen del festival Coca-Cola",
            Event.EventType.FESTIVAL,
            Event.EventStatus.AVAILABLE,
            1000
        );
        
        Event realMadrid = new Event(
            "Real Madrid vs Barcelona", 
            LocalDateTime.of(2025, 10, 20, 21, 0), 
            "Santiago Bernabéu",
            "Madrid", 
            "El clásico de la liga española", 
            "/img/deportes/Real-Madrid.webp",
            "Imagen del Santiago Bernabéu",
            Event.EventType.SPORTS,
            Event.EventStatus.AVAILABLE,
            10000
        );
        
        Event teatro = new Event(
            "Hamlet", 
            LocalDateTime.of(2025, 9, 10, 20, 0),
            "Teatro Español",
            "Barcelona", 
            "Representación de la obra clásica de Shakespeare", 
            "/img/teatros/teatroLara.webp",
            "Imagen del teatro Lara de Madrid",
            Event.EventType.THEATER,
            Event.EventStatus.SOLD_OUT,
            0
        );
        
        // Save events
        List<Event> events = Arrays.asList(quevedo, cocaColaFest, realMadrid, teatro);
        eventRepository.saveAll(events);
        
        // Create users
        User_ user1 = new User_("juan", "password123", "juan@example.com");
        //User_ user2 = new User_("maria", "securepass", "maria@example.com");
        //User_ user3 = new User_("admin", "admin123", "admin@reticket.com");
        
        userRepository.saveAll(Arrays.asList(user1/*, user2, user3*/));
        
    }
}
