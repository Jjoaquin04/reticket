package com.example.reticket.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.reticket.db.User_;

public interface UserRepository extends JpaRepository<User_,Long> {
    
    // Find user by username
    Optional<User_> findByUsername(String username);
    
    // Find user by email
    Optional<User_> findByEmail(String email);
    
    // Check if username exists
    boolean existsByUsername(String username);
    
    // Check if email exists
    boolean existsByEmail(String email);
}
