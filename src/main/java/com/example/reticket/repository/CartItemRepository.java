package com.example.reticket.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.reticket.db.CartItem;
import com.example.reticket.db.Event;
import com.example.reticket.db.ShoppingCart;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem,Long> {
    
    CartItem findCartItemByEvent(Event event);
    CartItem findCartItemByShoppingCart(ShoppingCart shoppingCart);
    CartItem findCartItemByEventAndShoppingCart(Event event, ShoppingCart shoppingCart);
    void deleteAllByShoppingCart(ShoppingCart shoppingCart);
}
