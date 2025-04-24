package com.example.reticket.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.reticket.db.ShoppingCart;
import com.example.reticket.db.User_;
import com.example.reticket.service.ShoppingCartService;
import com.example.reticket.service.UserService;

import jakarta.servlet.http.HttpSession;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class GetShoppingCartPage {

    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private UserService userService;
    
    @GetMapping("/shoppingCart")
    public ModelAndView getMethodName(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return new ModelAndView("redirect:/auth/login");
        }
        Optional<User_> user = userService.getUserById(userId);
        if(!user.isPresent()){
            return new ModelAndView("redirect:/auth/register");
        }
        ShoppingCart shoppingCart = shoppingCartService.getShoppingCartByUser(user.get());
        model.addAttribute("shoppingCart", shoppingCart);
        return new ModelAndView("carrito");
    }
    
}
