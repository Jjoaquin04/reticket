package com.example.reticket.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import com.example.reticket.service.EventService;

import jakarta.servlet.http.HttpSession;

import com.example.reticket.db.Event;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class GetMainPageController {
    
    @Autowired
    private EventService eventService;
    
    @GetMapping("/home")
    public ModelAndView mainPage(Model model, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return new ModelAndView("redirect:/auth/login");
        }
        List<Event> eventos = eventService.getAllEvents();
        model.addAttribute("mainEvents", eventos);
        return new ModelAndView("mainPage");
    }
}
