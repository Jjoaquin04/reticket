package com.example.reticket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping("/search")
public class SearchController {

    @GetMapping("")
    public RedirectView search(@RequestParam(required = false) String q) {
        try {
            // Si el término de búsqueda está vacío, redirigir a la página de eventos
            if (q == null || q.isEmpty()) {
                return new RedirectView("/events");
            }
            
            // Sanitizar y codificar el término de búsqueda para evitar problemas
            String sanitizedQuery = sanitizeSearchTerm(q);
            String encodedQuery = URLEncoder.encode(sanitizedQuery, StandardCharsets.UTF_8.toString());
            
            // Redirigir al controlador de eventos con el parámetro sanitizado y codificado
            return new RedirectView("/events?q=" + encodedQuery);
        } catch (Exception e) {
            // En caso de error en la sanitización o codificación, redirigir sin parámetro
            System.out.println("Error procesando término de búsqueda: " + e.getMessage());
            return new RedirectView("/events");
        }
    }
    
    private String sanitizeSearchTerm(String term) {
        if (term == null) return "";
        
        // Eliminar caracteres especiales problemáticos
        return term.replaceAll("[`\"'\\\\%;_]", "");
    }
}