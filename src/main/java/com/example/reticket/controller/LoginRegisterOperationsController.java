package com.example.reticket.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.reticket.db.User_;
import com.example.reticket.service.UserService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/auth")
public class LoginRegisterOperationsController {
    
    @Autowired
    private UserService userService;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String,String> credentials,HttpSession session){
        String username = credentials.get("username");
        String password = credentials.get("password");

        if(username == null || password == null || username.isEmpty() || password.isEmpty())    {
            return ResponseEntity.badRequest().body(Map.of("error","El usuario y la contraseña son obligatorios"));
        }

        Optional<User_>  userOptional = userService.getUserByName(username);
        if(userOptional.isPresent())    {
            User_ user = userOptional.get();
            if(passwordEncoder.matches(password, user.getPassword())){
                session.setAttribute("userId", user.getId());
                return ResponseEntity.ok().body(Map.of("success","Login exitoso"));
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
            "error","Usuario o contraseña incorrectos"
        ));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> userData,HttpSession session) {
        
        String username = userData.get("username");
        String email = userData.get("email");
        String password = userData.get("password");
        
        if (username == null || email == null || password == null || username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Todos los campos son obligatorios"));
        }
        if (userService.existsByUsername(username)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "El nombre de usuario ya está en uso"));
        }
        
        if (userService.existsByEmail(email)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "El email ya está registrado"));
        }
        try {
            String hashedPassword = passwordEncoder.encode(password);
            User_ newUser = new User_(username, hashedPassword, email);
            if (email.equals("admin@gmail.com")) {
                newUser.setUserType(User_.UserType.ADMIN);
            } else {
                newUser.setUserType(User_.UserType.USER);
            }
            User_ savedUser = userService.createUser(newUser);
            session.setAttribute("userId", savedUser.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "success","Usuario registrado exitosamente",
                "username" ,savedUser.getUsername()
                ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al registrar el usuario"));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok().body(Map.of("success", "Sesión cerrada correctamente"));
    }
}
