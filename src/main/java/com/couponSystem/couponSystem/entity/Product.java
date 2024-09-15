package com.couponSystem.couponSystem.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document("Product")
@Data
public class Product {

    @Field("id")
    private String id;

    @Field("price")
    private Double price;

    /* Getters and Setters */
}
