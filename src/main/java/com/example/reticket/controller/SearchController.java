package com.example.reticket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/search")
public class SearchController {

    @GetMapping("")
    public RedirectView search(@RequestParam(required = false) String q) {
        if (q == null || q.isEmpty()) {
            return new RedirectView("/events");
        }
        return new RedirectView("/events?q=" + q);
    }
}