package com.example.reticket.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.example.reticket.db.User_;
import com.example.reticket.service.UserService;

@RestController
@RequestMapping("/admin")
public class AdminUsersController {

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/users")
    public ResponseEntity<?> createUser(@RequestBody Map<String, String> userData) {
        // Verificar que el usuario actual es un administrador
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!auth.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Acceso denegado"));
        }

        String username = userData.get("username");
        String email = userData.get("email");
        String password = userData.get("password");
        String userTypeStr = userData.get("userType");

        if (username == null || email == null || password == null || userTypeStr == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Todos los campos son obligatorios"));
        }

        if (userService.existsByUsername(username)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "El nombre de usuario ya está en uso"));
        }

        if (userService.existsByEmail(email)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "El email ya está registrado"));
        }

        try {
            User_.UserType userType = User_.UserType.valueOf(userTypeStr);
            String hashedPassword = passwordEncoder.encode(password);
            User_ newUser = new User_(username, hashedPassword, email);
            newUser.setUserType(userType);
            
            User_ savedUser = userService.createUser(newUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al crear el usuario: " + e.getMessage()));
        }
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        // Verificar permisos
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!auth.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Acceso denegado"));
        }

        try {
            Optional<User_> userOpt = userService.getUserById(id);
            if (!userOpt.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            
            User_ user = userOpt.get();
            
            // Crear un mapa con solo los datos necesarios para evitar problemas de serialización
            Map<String, Object> userData = new HashMap<>();
            userData.put("id", user.getId());
            userData.put("username", user.getUsername() != null ? user.getUsername() : "");
            userData.put("email", user.getEmail() != null ? user.getEmail() : "");
            userData.put("userType", user.getUserType() != null ? user.getUserType().toString() : "USER");
            
            return ResponseEntity.ok(userData);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener datos del usuario: " + e.getMessage()));
        }
    }

    @PatchMapping("/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody Map<String, String> updates) {
        // Verificar permisos
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!auth.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Acceso denegado"));
        }

        Optional<User_> existingUser = userService.getUserById(id);
        if (!existingUser.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        User_ user = existingUser.get();
        
        String username = updates.get("username");
        String email = updates.get("email");
        String password = updates.get("password");
        String userTypeStr = updates.get("userType");

        // Validar y actualizar campos
        if (username != null) {
            if (!user.getUsername().equals(username) && userService.existsByUsername(username)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "El nombre de usuario ya está en uso"));
            }
            user.setUsername(username);
        }

        if (email != null) {
            if (!user.getEmail().equals(email) && userService.existsByEmail(email)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "El email ya está en uso"));
            }
            user.setEmail(email);
        }

        if (password != null && !password.isEmpty()) {
            user.setPassword(passwordEncoder.encode(password));
        }

        if (userTypeStr != null) {
            try {
                user.setUserType(User_.UserType.valueOf(userTypeStr));
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(Map.of("error", "Tipo de usuario no válido"));
            }
        }
        userService.updateUser(user);
        return ResponseEntity.ok().body(Map.of("success", "Usuario actualizado con éxito"));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        // Verificar permisos
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!auth.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Acceso denegado"));
        }

        Optional<User_> existingUser = userService.getUserById(id);
        if (!existingUser.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        // Evitar que el admin se elimine a sí mismo
        String currentUsername = auth.getName();
        if (existingUser.get().getUsername().equals(currentUsername)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "No puedes eliminar tu propia cuenta de administrador"));
        }

        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}