package com.example.reticket.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.reticket.db.CartItem;
import com.example.reticket.db.Event;
import com.example.reticket.db.ShoppingCart;
import com.example.reticket.db.Ticket;
import com.example.reticket.db.User_;
import com.example.reticket.service.CartItemService;
import com.example.reticket.service.EventService;
import com.example.reticket.service.ShoppingCartService;
import com.example.reticket.service.TicketService;
import com.example.reticket.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

@RestController
public class BuyTicketsController {

    @Autowired
    private UserService userService;
    @Autowired
    private TicketService ticketService;
    @Autowired
    private EventService eventService;
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private CartItemService cartItemService;

    @Transactional
    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Usuario no autenticado"));
        }
        Optional<User_> user = userService.getUserById(userId);
        if (!user.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Usuario no encontrado"));
        }
        ShoppingCart cart = shoppingCartService.getShoppingCartByUser(user.get());
        if (cart == null || cart.getCartItems().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Carrito de compras vacio o no encontrado"));
        }
        
        try {
            // Procesar todos los items
            for (CartItem item : new ArrayList<>(cart.getCartItems())) {

                Event event = item.getEvent();
                List<Ticket> purchasedTickets = new ArrayList<>();

                for(int i = 0; i < item.getQuantity(); i++) {
                    Ticket ticket = new Ticket(event,user.get());
                    purchasedTickets.add(ticket);
                    
                    // Actualizar el número de tickets disponibles en el evento
                    event.setCurrenNumberOfTickets(event.getCurrenNumberOfTickets() - 1);
                }

                ticketService.saveAllTickets(purchasedTickets);
                user.get().addListOfTickets(purchasedTickets);
                event.addListOfTickets(purchasedTickets);

                // Actualizar estado del evento si necesario
                if (event.getCurrenNumberOfTickets() <= 0) {
                    event.setEventStatus(Event.EventStatus.SOLD_OUT);
                }

                eventService.updateEvent(event);
            }
            userService.updateUser(user.get());
            // Limpiar el carrito después de la compra
            cart.getCartItems().clear();
            shoppingCartService.updateShoppingCart(cart);
            cartItemService.deleteAllByShoppingCart(cart);
            return ResponseEntity.ok(Map.of("message", "Compra realizada con éxito"));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error al procesar la compra"));
        }
    }
}
