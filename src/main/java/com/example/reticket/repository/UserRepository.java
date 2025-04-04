package com.example.reticket.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.reticket.db.User_;

public interface UserRepository extends JpaRepository<User_,Long> {
    
    
    Optional<User_> findByUsername(String username);
    Optional<User_> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    
    
}
