package com.example.reticket.repository;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.reticket.db.CartItem;
import com.example.reticket.db.Event;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem,Long> {
    
    CartItem findCartItemByEvent(Event event);
    CartItem findCartItemByShoppingCartId(Long ShoppingCartid);
    CartItem findCartItemByEventIdAndShoppingCartId(Long eventId, Long shoppingCartId);
    List<CartItem> findAllByShoppingCartId(Long shoppingCartId);
    List<CartItem> findAllByEventId(Long eventId);
    void deleteAllByShoppingCartId(Long shoppingCartId);
    void deleteAllByEventId(Long eventId);
}
