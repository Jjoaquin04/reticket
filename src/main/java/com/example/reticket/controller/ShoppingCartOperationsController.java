package com.example.reticket.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.reticket.db.CartItem;
import com.example.reticket.db.Event;
import com.example.reticket.db.ShoppingCart;
import com.example.reticket.db.User_;
import com.example.reticket.service.CartItemService;
import com.example.reticket.service.EventService;
import com.example.reticket.service.ShoppingCartService;
import com.example.reticket.service.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

@RestController
@Transactional
public class ShoppingCartOperationsController {

    @Autowired
    private UserService userService;
    @Autowired
    private CartItemService cartItemService;
    @Autowired
    private EventService eventService;
    @Autowired
    private ShoppingCartService shoppingCartService;
    
    @PostMapping("/addShoppingCart/{eventId}")
    public ResponseEntity<?> addShoppingCart(@PathVariable Long eventId, HttpSession session) {
        // Comprobar que el usuario está autenticado
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error","Usuario no autenticado"));
        }
        
        Optional<User_> user = userService.getUserById(userId);
        if(!user.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error","Usuario no encontrado"));
        }
        
        Optional<Event> existingEvent = eventService.getEventById(eventId);
        if(!existingEvent.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Evento no encontrado"));
        }
        if(existingEvent.get().getCurrenNumberOfTickets() <= 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "No hay entradas disponibles"));
        }
        
        // Obtener el carrito de compras si existe
        ShoppingCart shoppingCart = shoppingCartService.getShoppingCartByUser(user.get());
        if (shoppingCart == null) {
            shoppingCart = new ShoppingCart(user.get());
            shoppingCartService.createShoppingCart(shoppingCart);
        }
        
        CartItem existingCartItem = cartItemService.getCartItemByEventAndShoppingCart(existingEvent.get(),shoppingCart);
        
        if(existingCartItem != null) {
            // Incrementar la cantidad si ya existe
            existingCartItem.setQuantity(existingCartItem.getQuantity() + 1);
            cartItemService.updateCartItem(existingCartItem);  
        } else {
            // Crear un nuevo CartItem
            CartItem cartItem = new CartItem(existingEvent.get(), shoppingCart, 1);
            cartItemService.createCartItem(cartItem);
            shoppingCart.addCartItem(cartItem);
            shoppingCartService.updateShoppingCart(shoppingCart); 
        }

        existingEvent.get().setCurrenNumberOfTickets(existingEvent.get().getCurrenNumberOfTickets() - 1);
        // Actualizar el estado del evento si es necesario
        if (existingEvent.get().getCurrenNumberOfTickets() <= 0) {
            existingEvent.get().setEventStatus(Event.EventStatus.SOLD_OUT);
        }
        eventService.updateEvent(existingEvent.get());
        
        return ResponseEntity.ok(Map.of("message", "Evento añadido al carrito de compras"));
    }


    @DeleteMapping("/cartItems/{cartItemId}")
    public ResponseEntity<?> deleteCartItem(@PathVariable Long cartItemId, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error","Usuario no autenticado"));
        }
        
        Optional<User_> user = userService.getUserById(userId);
        ShoppingCart shoppingCart = shoppingCartService.getShoppingCartByUser(user.get());
        if (shoppingCart == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Carrito de compras no encontrado"));
        }
        
        CartItem item = cartItemService.getCartItemById(cartItemId);
        if (item == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Item no encontrado en el carrito"));
        }
        
        // Restaurar la cantidad de tickets disponibles
        Event event = item.getEvent();
        int quantity = item.getQuantity();
        event.setCurrenNumberOfTickets(event.getCurrenNumberOfTickets() + quantity);
        
        // Actualizar el estado del evento si es necesario
        if (event.getEventStatus() == Event.EventStatus.SOLD_OUT && event.getCurrenNumberOfTickets() > 0) {
            event.setEventStatus(Event.EventStatus.AVAILABLE);
        }
        
        eventService.updateEvent(event);
        
        cartItemService.deleteCartItem(cartItemId);
        shoppingCart.getCartItems().remove(item);
        shoppingCartService.updateShoppingCart(shoppingCart);
        return ResponseEntity.ok(Map.of("message", "Item eliminado del carrito de compras"));
    }
    @PatchMapping("/cartItems/{cartItemId}/{quantity}")
    public ResponseEntity<?> updateCartItem(@PathVariable Long cartItemId, @PathVariable int quantity, HttpSession session) {
        // Optimización: si la cantidad es 0, usar el método de eliminar directamente
        if (quantity <= 0) {
            return deleteCartItem(cartItemId, session);
        }
        
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error","Usuario no autenticado"));
        }
        
        // Optimización: cargar solo lo necesario
        CartItem item = cartItemService.getCartItemById(cartItemId);
        if (item == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Item no encontrado en el carrito"));
        }
        
        // Calcular la diferencia entre la cantidad actual y la nueva cantidad
        int oldQuantity = item.getQuantity();
        int diff = oldQuantity - quantity;
        
        Event event = item.getEvent();
        int newTicketsAvailable = event.getCurrenNumberOfTickets() + diff;
        
        // Verificar si hay suficientes tickets disponibles para aumentar la cantidad
        if (diff < 0 && newTicketsAvailable < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", 
                "No hay suficientes tickets disponibles. Disponibles: " + event.getCurrenNumberOfTickets()));
        }
        
        // Actualizar el número de tickets disponibles en el evento
        event.setCurrenNumberOfTickets(newTicketsAvailable);
        
        // Actualizar el estado del evento si es necesario
        if (diff > 0 && event.getEventStatus() == Event.EventStatus.SOLD_OUT && newTicketsAvailable > 0) {
            event.setEventStatus(Event.EventStatus.AVAILABLE);
        } else if (diff < 0 && newTicketsAvailable <= 0) {
            event.setEventStatus(Event.EventStatus.SOLD_OUT);
        }
        
        // Actualizar la cantidad del item primero
        item.setQuantity(quantity);
        
        // Guardar los cambios en una sola transacción
        try {
            cartItemService.updateCartItem(item);
            eventService.updateEvent(event);
            return ResponseEntity.ok(Map.of("message", "Cantidad actualizada"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al actualizar: " + e.getMessage()));
        }
    }

    
}
