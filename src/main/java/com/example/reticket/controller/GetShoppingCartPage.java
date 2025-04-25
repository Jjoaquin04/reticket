package com.example.reticket.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.reticket.db.CartItem;
import com.example.reticket.db.ShoppingCart;
import com.example.reticket.db.User_;
import com.example.reticket.service.CartItemService;
import com.example.reticket.service.ShoppingCartService;
import com.example.reticket.service.UserService;
import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
public class GetShoppingCartPage {

    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private UserService userService;
    @Autowired
    private CartItemService cartItemService;
    
    @GetMapping("/shoppingCart")
    public ModelAndView mainPage(HttpSession session,Model model)    {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return new ModelAndView("redirect:/auth/login");
        }
        Optional<User_> user = userService.getUserById(userId);
        if(!user.isPresent()){
            return new ModelAndView("redirect:/auth/register");
        }
        ShoppingCart shoppingCart = shoppingCartService.getShoppingCartByUser(user.get());
        if (shoppingCart == null) {
            shoppingCart = new ShoppingCart(user.get());
            shoppingCartService.createShoppingCart(shoppingCart);
        }
        List<CartItem> cartItems = cartItemService.getAllByShoppingCart(shoppingCart);
        model.addAttribute("cartItems", cartItems);
        return new ModelAndView("carritoPage");
    }
    
}
