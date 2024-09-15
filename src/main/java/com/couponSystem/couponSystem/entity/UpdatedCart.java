package com.couponSystem.couponSystem.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdatedCart extends Cart {

    private List<UpdatedCartItem> updatedItems;
    private double totalPrice;
    private double totalDiscount;
    private double finalPrice;

}