package com.example.reticket.controller;

import java.util.List;

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
    public ModelAndView getProfile(Model model) {
        List<User_> allUsers = userService.getAllUsers();
        model.addAttribute("users", allUsers);
        return new ModelAndView("profilePage");
    }
    @GetMapping("/myTickets")
    public ModelAndView getTickets(Model model) {
        List<Ticket> allTickets = ticketService.getAllTickets();
        model.addAttribute("tickets",allTickets);
        return new ModelAndView("myTicketsPage");
    }
    @GetMapping("/newEvent")
    public ModelAndView getNewEventPage() {
        return new ModelAndView("newEventPage");
    }
}
