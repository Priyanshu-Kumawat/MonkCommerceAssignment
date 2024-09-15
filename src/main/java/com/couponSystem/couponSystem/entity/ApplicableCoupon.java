package com.couponSystem.couponSystem.entity;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApplicableCoupon {

    private String couponId;
    private CouponType type;
    private double discount;

    @Override
    public String toString() {
        return "ApplicableCoupon{" +
                "couponId='" + couponId + '\'' +
                ", type=" + type +
                ", discount=" + discount +
                '}';
    }

}
