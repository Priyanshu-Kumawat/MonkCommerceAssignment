package com.couponSystem.couponSystem.services;

import com.couponSystem.couponSystem.Exceptions.ResourceNotFoundException;
import com.couponSystem.couponSystem.Exceptions.ServiceException;
import com.couponSystem.couponSystem.entity.ApplicableCoupon;
import com.couponSystem.couponSystem.entity.Cart;
import com.couponSystem.couponSystem.entity.Coupon;
import com.couponSystem.couponSystem.entity.UpdatedCart;

import java.util.List;

public interface CouponService {

    Coupon createCoupon(Coupon coupon) throws ServiceException;

    List<Coupon> getAllCoupons() throws ServiceException;
//
    Coupon getCouponById(String id) throws ResourceNotFoundException, ServiceException;

    Coupon updateCoupon(String id, Coupon couponDetails) throws ResourceNotFoundException, ServiceException;

    void deleteCoupon(String id) throws ResourceNotFoundException, ServiceException;

    List<ApplicableCoupon> getApplicableCoupons(Cart cart) throws ServiceException;

    UpdatedCart applyCouponToCart(String couponId, Cart cart) throws ResourceNotFoundException, ServiceException;

}
