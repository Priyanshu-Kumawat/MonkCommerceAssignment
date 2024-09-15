package com.couponSystem.couponSystem.mappers;

import com.couponSystem.couponSystem.entity.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.couponSystem.couponSystem.entity.CouponDetails;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;



public class CouponDetailsDeserializer extends JsonDeserializer<Coupon> {

    @Override
    public Coupon deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException {
        ObjectMapper mapper = (ObjectMapper) p.getCodec();
        JsonNode node = mapper.readTree(p);


        String type = node.get("type").asText();


        JsonNode detailsNode = node.get("details");

        CouponDetails details;
        switch (type) {
            case "PRODUCT_WISE":
                details = mapper.treeToValue(detailsNode, ProductWiseCouponDetails.class);
                break;
            case "CART_WISE":
                details = mapper.treeToValue(detailsNode, CartWiseCouponDetails.class);
                break;
            case "BXGY":
                details = mapper.treeToValue(detailsNode, BxGyCouponDetails.class);
                break;
            default:
                throw new IllegalArgumentException("Unknown coupon type: " + type);
        }


        Coupon coupon = new Coupon();
        coupon.setType(CouponType.valueOf(type));
        coupon.setDetails(details);

        return coupon;
    }
}
