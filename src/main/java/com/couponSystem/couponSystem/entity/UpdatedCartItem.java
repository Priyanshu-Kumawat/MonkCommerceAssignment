package com.couponSystem.couponSystem.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class UpdatedCartItem extends CartItem {
    private double totalDiscount;


    // Constructor that accepts a CartItem and the total discount
    public UpdatedCartItem(CartItem cartItem, double discount) {

        this.setProductId(cartItem.getProductId());
        this.setQuantity(cartItem.getQuantity());
        this.setPrice(cartItem.getPrice());
        this.totalDiscount = discount;
    }

}