package com.couponSystem.couponSystem.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Data
public class CartItem {


    private long productId;

    private int quantity;

    private double price;


}
