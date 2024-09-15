package com.couponSystem.couponSystem.controllers;

import com.couponSystem.couponSystem.Exceptions.*;
import com.couponSystem.couponSystem.entity.*;
import com.couponSystem.couponSystem.services.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coupons")
public class CouponController {

    private final CouponServiceImpl couponService;

    public CouponController(CouponServiceImpl couponService) {
        this.couponService = couponService;
    }


    // 1. Create a new coupon
    @PostMapping
    public ResponseEntity<Coupon> createCoupon(@RequestBody Coupon coupon) {
        try {
            System.out.println("Received Coupon: " + coupon);
            Coupon createdCoupon = couponService.createCoupon(coupon);
            return new ResponseEntity<>(createdCoupon, HttpStatus.CREATED);
        } catch (ServiceException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 2. Retrieve all coupons
    @GetMapping
    public ResponseEntity<List<Coupon>> getAllCoupons() {
        try {
            List<Coupon> coupons = couponService.getAllCoupons();
            return new ResponseEntity<>(coupons, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 3. Retrieve a specific coupon by its ID
    @GetMapping("/{id}")
    public ResponseEntity<Coupon> getCouponById(@PathVariable String id) {
        try {
            Coupon coupon = couponService.getCouponById(id);
            if (coupon != null) {
                return new ResponseEntity<>(coupon, HttpStatus.OK);
            } else {
                throw new ResourceNotFoundException("Coupon not found with id: " + id);
            }
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    //     4. Update a specific coupon by its ID
    @PutMapping("/{id}")
    public ResponseEntity<Coupon> updateCoupon(@PathVariable String id, @RequestBody Coupon couponDetails) {
        try {
            Coupon updatedCoupon = couponService.updateCoupon(id, couponDetails);
            if (updatedCoupon != null) {
                return new ResponseEntity<>(updatedCoupon, HttpStatus.OK);
            } else {
                throw new ResourceNotFoundException("Coupon not found with id: " + id);
            }
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (ServiceException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 5. Delete a specific coupon by its ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCoupon(@PathVariable String id) {
        try {
            couponService.deleteCoupon(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (ServiceException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 6. Fetch all applicable coupons for a given cart and calculate the total discount
    @PostMapping("/applicable-coupons")
    public ResponseEntity<List<ApplicableCoupon>> getApplicableCoupons(@RequestBody Cart cart) {
        try {
            List<ApplicableCoupon> applicableCoupons = couponService.getApplicableCoupons(cart);
            return new ResponseEntity<>(applicableCoupons, HttpStatus.OK);
        } catch (ServiceException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 7. Apply a specific coupon to the cart and return the updated cart with discounted prices
    @PostMapping("/apply-coupon/{id}")
    public ResponseEntity<UpdatedCart> applyCouponToCart(@PathVariable String id, @RequestBody Cart cart) {
        try {
            UpdatedCart updatedCart = couponService.applyCouponToCart(id, cart);
            return new ResponseEntity<>(updatedCart, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (ServiceException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
