package com.couponSystem.couponSystem.controllers;

import com.couponSystem.couponSystem.controllers.CouponController;
import com.couponSystem.couponSystem.entity.Coupon;
import com.couponSystem.couponSystem.entity.Cart;
import com.couponSystem.couponSystem.entity.UpdatedCart;
import com.couponSystem.couponSystem.entity.CouponType;
import com.couponSystem.couponSystem.entity.ProductWiseCouponDetails;
import com.couponSystem.couponSystem.services.CouponServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CouponController.class)
public class CouponControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CouponServiceImpl couponService;

    private Coupon mockCoupon;

    @BeforeEach
    public void setup() {
        mockCoupon = new Coupon();
        mockCoupon.setId("1");
        mockCoupon.setType(CouponType.PRODUCT_WISE);
        mockCoupon.setDetails(new ProductWiseCouponDetails(1, 10));
    }

    // Test for creating a coupon
    @Test
    public void createCoupon() throws Exception {
        when(couponService.createCoupon(any(Coupon.class))).thenReturn(mockCoupon);

        String couponJson = "{\"type\":\"PRODUCT_WISE\",\"details\":{\"productId\":1,\"discount\":10}}";

        mockMvc.perform(post("/api/coupons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(couponJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.type").value("PRODUCT_WISE"))
                .andExpect(jsonPath("$.details.productId").value(1))
                .andExpect(jsonPath("$.details.discount").value(10));
    }

    // Test for retrieving all coupons
    @Test
    public void getAllCoupons() throws Exception {
        List<Coupon> coupons = Collections.singletonList(mockCoupon);
        when(couponService.getAllCoupons()).thenReturn(coupons);

        mockMvc.perform(get("/api/coupons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].type").value("PRODUCT_WISE"))
                .andExpect(jsonPath("$[0].details.productId").value(1))
                .andExpect(jsonPath("$[0].details.discount").value(10));
    }

    // Test for retrieving a coupon by ID
    @Test
    public void getCouponById() throws Exception {
        when(couponService.getCouponById("1")).thenReturn(mockCoupon);

        mockMvc.perform(get("/api/coupons/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("PRODUCT_WISE"))
                .andExpect(jsonPath("$.details.productId").value(1))
                .andExpect(jsonPath("$.details.discount").value(10));
    }

    // Test for updating a coupon
    @Test
    public void updateCoupon() throws Exception {
        Coupon updatedCoupon = new Coupon();
        updatedCoupon.setId("1");
        updatedCoupon.setType(CouponType.PRODUCT_WISE);
        updatedCoupon.setDetails(new ProductWiseCouponDetails(2, 20)); // Updated details: productId=2, discount=20

        when(couponService.updateCoupon(Mockito.eq("1"), any(Coupon.class))).thenReturn(updatedCoupon);

        String updatedCouponJson = "{\"type\":\"PRODUCT_WISE\",\"details\":{\"productId\":2,\"discount\":20}}";

        mockMvc.perform(put("/api/coupons/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedCouponJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("PRODUCT_WISE"))
                .andExpect(jsonPath("$.details.productId").value(2))
                .andExpect(jsonPath("$.details.discount").value(20));
    }

    // Test for deleting a coupon by ID
    @Test
    public void deleteCoupon() throws Exception {
        Mockito.doNothing().when(couponService).deleteCoupon("1");

        mockMvc.perform(delete("/api/coupons/1"))
                .andExpect(status().isNoContent());
    }

    // Test for applying a coupon to a cart
    @Test
    public void applyCouponToCart() throws Exception {
        UpdatedCart updatedCart = new UpdatedCart();
        updatedCart.setFinalPrice(100.0);

        when(couponService.applyCouponToCart(Mockito.eq("1"), any(Cart.class))).thenReturn(updatedCart);

        String cartJson = "{\"items\":[]}";

        mockMvc.perform(post("/api/coupons/apply-coupon/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cartJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.finalPrice").value(100.0));
    }
}
