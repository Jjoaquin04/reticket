package com.example.reticket.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.reticket.db.User_;
import com.example.reticket.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User_ createUser(User_ user) {
        return userRepository.save(user);
    }
    public List<User_> getAllUsers(){
        return userRepository.findAll();
    }
    public Optional<User_> getUserByName(String username) {
        return userRepository.findByUsername(username);
    }
    public Optional<User_> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
    public User_ updateUser(User_ user) {
        return userRepository.save(user);
    }
    public Optional<User_> getUserById(Long id) {
        return userRepository.findById(id);
    }
}   
