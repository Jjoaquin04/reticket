package com.example.reticket.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.reticket.db.CartItem;
import com.example.reticket.db.Event;
import com.example.reticket.db.ShoppingCart;
import com.example.reticket.repository.CartItemRepository;

@Service
public class CartItemService {
    
    @Autowired
    private CartItemRepository cartItemRepository;

    public CartItem createCartItem(CartItem cartItem) {
        return cartItemRepository.save(cartItem);
    }
    public CartItem getCartItemById(Long id) {
        return cartItemRepository.findById(id).orElse(null);
    }
    public CartItem getCartItemByEvent(Event event) {
        return cartItemRepository.findCartItemByEvent(event);
    }
    public CartItem getCartItemByShoppingCart(ShoppingCart shoppingCart) {
        return cartItemRepository.findCartItemByShoppingCartId(shoppingCart.getId());
    }
    public List<CartItem> getAllByShoppingCart(ShoppingCart shoppingCart) {
        return cartItemRepository.findAllByShoppingCartId(shoppingCart.getId());
    }
    public List<CartItem> getAllByEvent(Event event) {
        return cartItemRepository.findAllByEventId(event.getId());
    }
    public CartItem getCartItemByEventAndShoppingCart(Event event, ShoppingCart shoppingCart) {
        return cartItemRepository.findCartItemByEventIdAndShoppingCartId(event.getId(), shoppingCart.getId());
    }
    public CartItem updateCartItem(CartItem cartItem) {
        return cartItemRepository.save(cartItem);
    }
    public void deleteCartItem(Long id) {
        cartItemRepository.deleteById(id);
    }
    public void deleteAllByShoppingCart(ShoppingCart shoppingCart) {
        cartItemRepository.deleteAllByShoppingCartId(shoppingCart.getId());
    }
    public void deleteAllByEvent(Event event) {
        cartItemRepository.deleteAllByEventId(event.getId());
    }
}
