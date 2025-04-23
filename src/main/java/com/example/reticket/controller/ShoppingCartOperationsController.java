package com.example.reticket.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

@RestController
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
        // Check if user is authenticated
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error","Usuario no autenticado"));
        }
        
        // Find the user
        Optional<User_> user = userService.getUserById(userId);
        if(!user.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error","Usuario no encontrado"));
        }
        
        // Find the event
        Optional<Event> existingEvent = eventService.getEventById(eventId);
        if(!existingEvent.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Evento no encontrado"));
        }
        
        // Get or create shopping cart for user
        ShoppingCart shoppingCart = shoppingCartService.getShoppingCartByUser(user.get());
        if (shoppingCart == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Carrito de compras no encontrado"));
        }
        
        // Check if the event is already in the user's cart
        CartItem existingCartItem = cartItemService.getCartItemByEventAndShoppingCart(existingEvent.get(),shoppingCart);
        
        if(existingCartItem != null) {
            // Update quantity if item already in cart
            existingCartItem.setQuantity(existingCartItem.getQuantity() + 1);
            cartItemService.updateCartItem(existingCartItem);  
        } else {
            // Create new cart item if not
            CartItem cartItem = new CartItem(existingEvent.get(), shoppingCart, 1);
            cartItemService.createCartItem(cartItem);
            shoppingCart.addCartItem(cartItem);
            shoppingCartService.updateShoppingCart(shoppingCart); 
        }
        
        return ResponseEntity.ok(Map.of("message", "Evento añadido al carrito de compras"));
    }
}
