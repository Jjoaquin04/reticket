
package com.example.reticket.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.reticket.db.ShoppingCart;
import com.example.reticket.db.User_;
import com.example.reticket.repository.ShoppingCartRepository;

@Service
public class ShoppingCartService {

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    // Create
    public ShoppingCart createShoppingCart(ShoppingCart shoppingCart) {
        return shoppingCartRepository.save(shoppingCart);
    }
    public Optional<ShoppingCart> getShoppingCartById(Long id) {
        return shoppingCartRepository.findById(id);
    }
    public ShoppingCart getShoppingCartByUser(User_ user) {
        return shoppingCartRepository.findShoppingCartByUser(user);
    }
    public ShoppingCart updateShoppingCart(ShoppingCart shoppingCart) {
        return shoppingCartRepository.save(shoppingCart);
    }
    public void deleteShoppingCart(Long id) {
        shoppingCartRepository.deleteById(id);
    }
}