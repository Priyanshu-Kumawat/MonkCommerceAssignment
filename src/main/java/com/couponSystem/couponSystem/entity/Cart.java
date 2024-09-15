package com.couponSystem.couponSystem.entity;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document(collection = "cart")
@Data
public class Cart {

    @Id
    private String id;

    private List<CartItem> items;

    // Getters and Setters
}