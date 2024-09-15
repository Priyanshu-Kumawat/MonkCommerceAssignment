package com.couponSystem.couponSystem.entity;

import com.couponSystem.couponSystem.mappers.CouponDetailsDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "coupons")
@Data
@JsonDeserialize(using = CouponDetailsDeserializer.class)
public class Coupon {

    @Id
    private String id;

    @Field("type")
    private CouponType type;

    @Field("details")
    private CouponDetails details;

}