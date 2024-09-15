package com.couponSystem.couponSystem.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@AllArgsConstructor
public class ProductWiseCouponDetails extends CouponDetails {
    @Field("productId")
    private long productId;

    @Field("discount")
    private double discount;

}