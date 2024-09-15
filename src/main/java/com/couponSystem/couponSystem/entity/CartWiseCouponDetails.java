package com.couponSystem.couponSystem.entity;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.*;

@Data
@AllArgsConstructor
public class CartWiseCouponDetails extends CouponDetails {


    @Field("threshold")
    private double threshold;

    @Field("discount")
    private double discount;

}
