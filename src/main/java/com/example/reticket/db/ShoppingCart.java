package com.example.reticket.db;

import java.util.HashMap;

import jakarta.annotation.Generated;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class ShoppingCart {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
}
