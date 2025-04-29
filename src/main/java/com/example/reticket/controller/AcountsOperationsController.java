package com.example.reticket.controller;

import org.springframework.web.bind.annotation.RestController;
import com.example.reticket.db.Event;
import com.example.reticket.db.User_;
import com.example.reticket.service.EventService;
import com.example.reticket.service.UserService;
import com.example.reticket.db.CartItem;
import com.example.reticket.service.CartItemService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class AcountsOperationsController {
    
    @Autowired
    private EventService eventService;
    @Autowired
    private UserService userService;
    @Autowired
    private CartItemService cartItemService;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional
    @PostMapping("/submitEvent")
    public ResponseEntity<?> createEvent(@RequestBody(required = true) Event event, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Usuario no autenticado"));
        }
        
        if (event.getPrice() < 0.0) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(Map.of("error", "El precio no puede ser negativo"));
        }
        
        Optional<User_> creatorOpt = userService.getUserById(userId);
        if (!creatorOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Usuario no encontrado"));
        }
        
        // Asignar el creador al evento
        event.setCreator(creatorOpt.get());
        
        Event newEvent = eventService.createEvent(event);
        return ResponseEntity.status(HttpStatus.CREATED).body(newEvent);
    }

    @Transactional
    @DeleteMapping("events/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable Long id) {
        Optional<Event> deletedEvent = eventService.getEventById(id);
        if(deletedEvent.isPresent())    {
            eventService.deleteEvent(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Transactional
    @PutMapping("events/{id}")
    public ResponseEntity<?> updateEvent(@PathVariable Long id, @RequestBody Event newEvent) {
        Optional<Event> event = eventService.getEventById(id);
        if(event != null)   {
            newEvent.setId(id);
            eventService.updateEvent(newEvent);
            return ResponseEntity.ok(newEvent);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @Transactional
    @PatchMapping("events/{id}")
    public ResponseEntity<?> updateEventStatus(@PathVariable Long id, @RequestBody Event.EventStatus status) {
        Optional<Event> event = eventService.getEventById(id);
        if(event.isPresent()){
            Event updatedEvent = event.get();
            Event.EventStatus oldStatus = updatedEvent.getEventStatus();
            
            // Si el evento se cancela o finaliza, limpiar los CartItems relacionados
            if ((status == Event.EventStatus.CANCELLED || status == Event.EventStatus.FINISHED) && 
                oldStatus != Event.EventStatus.CANCELLED && oldStatus != Event.EventStatus.FINISHED) {
                
                // Eliminar los CartItems y actualizar el contador de tickets
                removeCartItemsAndRestoreTickets(updatedEvent);
            }
            
            updatedEvent.setEventStatus(status);
            eventService.updateEvent(updatedEvent);
            return ResponseEntity.ok().body(Map.of("status", updatedEvent.getEventStatus().toString()));
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    /*
    Borrar todos los cartItems si el evento se cancela o finaliza
    */
    private void removeCartItemsAndRestoreTickets(Event event) {
        // Obtener todos los CartItems para este evento
        List<CartItem> cartItems = cartItemService.getAllByEvent(event);
        
        // Calcular cuántos tickets hay que devolver al inventario
        int ticketsToReturn = 0;
        for (CartItem item : cartItems) {
            ticketsToReturn += item.getQuantity();
        }
        event.setCurrenNumberOfTickets(event.getCurrenNumberOfTickets() + ticketsToReturn);
        cartItemService.deleteAllByEvent(event);
    }

    @Transactional
    @PatchMapping("/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id,@RequestBody Map<String, Object> updates) {
        Optional<User_> existingUser = userService.getUserById(id);
        if (!existingUser.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        User_ user = existingUser.get();
        
        if (updates.containsKey("username")) {
            String newUsername = (String) updates.get("username");
            
            // Verificar que el nuevo username no exista ya
            if (!user.getUsername().equals(newUsername) && userService.existsByUsername(newUsername)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error","El nombre de usuario ya está en uso"));
            }
            user.setUsername(newUsername);
        }
        
        if (updates.containsKey("email")) {
            String newEmail = (String) updates.get("email");
            
            // Verificar que el nuevo email no exista ya
            if (!user.getEmail().equals(newEmail) && userService.existsByEmail(newEmail)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error","El email ya está en uso"));
            }
            user.setEmail(newEmail);
        }
        
        if (updates.containsKey("password")) {
            String newPassword = (String) updates.get("password");
            if(passwordEncoder.matches(newPassword, user.getPassword())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error","La contraseña no puede ser la misma que la anterior"));
            }
            
            user.setPassword(passwordEncoder.encode(newPassword));
        }
        
        User_ updatedUser = userService.updateUser(user);

        // No retornar la contraseña por seguridad
        Map<String, Object> response = Map.of(
            "username", updatedUser.getUsername(),
            "email", updatedUser.getEmail()
        );
        
        return ResponseEntity.ok(response);
    }

}

