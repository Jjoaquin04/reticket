package com.example.reticket.controller;

import java.util.List;
import java.util.Optional;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.reticket.db.Event;
import com.example.reticket.db.Ticket;
import com.example.reticket.db.User_;
import com.example.reticket.service.EventService;
import com.example.reticket.service.TicketService;
import com.example.reticket.service.UserService;

@RestController
@RequestMapping("/acounts")
public class GetAcountsOptionsPageController {
    @Autowired
    private EventService eventService;
    @Autowired
    private UserService userService;
    @Autowired
    private TicketService ticketService;
   
    @GetMapping("/myEvents")
    public ModelAndView getEvents(Model model) {
        List<Event> allEvents = eventService.getAllEvents();
        model.addAttribute("events", allEvents);
        return new ModelAndView("myEventsPage");
    }
    @GetMapping("/profile")
    public ModelAndView getProfile(Model model, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId != null) {
            Optional<User_> user = userService.getUserById(userId);
            if (user.isPresent()) {
                model.addAttribute("users", List.of(user.get()));
                return new ModelAndView("profilePage");
            }
        }
        return new ModelAndView("redirect:/");
    }
    @GetMapping("/myTickets")
    public ModelAndView getTickets(Model model, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId != null) {
            List<Ticket> userTickets = ticketService.getTicketsByUserId(userId);
            model.addAttribute("tickets", userTickets);
            return new ModelAndView("myTicketsPage");
        }
        return new ModelAndView("redirect:/");
    }
    @GetMapping("/newEvent")
    public ModelAndView getNewEventPage() {
        return new ModelAndView("newEventPage");
    }
}
