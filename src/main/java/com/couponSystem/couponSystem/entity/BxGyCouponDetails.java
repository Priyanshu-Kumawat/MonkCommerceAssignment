package com.couponSystem.couponSystem.entity;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.*;

import java.util.List;

@Data
public class BxGyCouponDetails extends CouponDetails {


    @Field("buyProducts")
    private List<ProductQuantity> buyProducts;

    @Field("getProducts")
    private List<ProductQuantity> getProducts;

    @Field("repetitionLimit")
    private int repetitionLimit;

}