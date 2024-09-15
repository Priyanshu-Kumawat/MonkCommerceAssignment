package com.couponSystem.couponSystem.services;

import com.couponSystem.couponSystem.Exceptions.*;
import com.couponSystem.couponSystem.entity.*;
import com.couponSystem.couponSystem.repositories.CouponRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.extension.ExtendWith;

import java.util.*;

@ExtendWith(MockitoExtension.class)
class CouponServiceImplTest {

    @Mock
    private CouponRepository couponRepository;

    @InjectMocks
    private CouponServiceImpl couponService;

    private Coupon sampleCoupon;

    @BeforeEach
    void setUp() {
        sampleCoupon = new Coupon();
        sampleCoupon.setId("1");
        sampleCoupon.setType(CouponType.CART_WISE);
        sampleCoupon.setDetails(new CartWiseCouponDetails(100, 10));
    }

    @Test
    void testCreateCoupon_Success() throws ServiceException {
        when(couponRepository.save(any(Coupon.class))).thenReturn(sampleCoupon);

        Coupon createdCoupon = couponService.createCoupon(sampleCoupon);

        assertNotNull(createdCoupon);
        assertEquals("1", createdCoupon.getId());
        verify(couponRepository, times(1)).save(sampleCoupon);
    }

    @Test
    void testCreateCoupon_Failure() {
        when(couponRepository.save(any(Coupon.class))).thenThrow(new RuntimeException("DB error"));

        assertThrows(ServiceException.class, () -> couponService.createCoupon(sampleCoupon));
    }

    @Test
    void testGetAllCoupons_Success() throws ServiceException {
        List<Coupon> couponList = Collections.singletonList(sampleCoupon);
        when(couponRepository.findAll()).thenReturn(couponList);

        List<Coupon> result = couponService.getAllCoupons();

        assertEquals(1, result.size());
        verify(couponRepository, times(1)).findAll();
    }

    @Test
    void testGetAllCoupons_Failure() {
        when(couponRepository.findAll()).thenThrow(new RuntimeException("DB error"));

        assertThrows(ServiceException.class, () -> couponService.getAllCoupons());
    }

    @Test
    void testGetCouponById_Success() throws ResourceNotFoundException, ServiceException {
        when(couponRepository.findById("1")).thenReturn(Optional.of(sampleCoupon));

        Coupon coupon = couponService.getCouponById("1");

        assertNotNull(coupon);
        assertEquals("1", coupon.getId());
    }

    @Test
    void testGetCouponById_NotFound() {
        when(couponRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> couponService.getCouponById("1"));
    }

    @Test
    void testGetCouponById_ServiceException() {
        when(couponRepository.findById("1")).thenThrow(new RuntimeException("DB error"));

        assertThrows(ServiceException.class, () -> couponService.getCouponById("1"));
    }

    @Test
    void testUpdateCoupon_Success() {
        when(couponRepository.findById("1")).thenReturn(Optional.of(sampleCoupon));
        when(couponRepository.save(any(Coupon.class))).thenReturn(sampleCoupon);

        Coupon updatedCoupon = new Coupon();
        updatedCoupon.setType(CouponType.PRODUCT_WISE);
        updatedCoupon.setDetails(new ProductWiseCouponDetails(1L, 10.0));

        Coupon result = couponService.updateCoupon("1", updatedCoupon);

        assertEquals(CouponType.PRODUCT_WISE, result.getType());
        verify(couponRepository, times(1)).findById("1");
        verify(couponRepository, times(1)).save(any(Coupon.class));
    }

    @Test
    void testUpdateCoupon_NotFound() {
        when(couponRepository.findById("1")).thenReturn(Optional.empty());

        Coupon updatedCoupon = new Coupon();
        updatedCoupon.setType(CouponType.PRODUCT_WISE);

        assertThrows(CouponNotFoundException.class, () -> couponService.updateCoupon("1", updatedCoupon));
    }

    @Test
    void testDeleteCoupon_Success() throws ResourceNotFoundException, ServiceException {
        when(couponRepository.findById("1")).thenReturn(Optional.of(sampleCoupon));

        couponService.deleteCoupon("1");

        verify(couponRepository, times(1)).delete(sampleCoupon);
    }

    @Test
    void testDeleteCoupon_NotFound() {
        when(couponRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> couponService.deleteCoupon("1"));
    }

    @Test
    void testDeleteCoupon_ServiceException() {
        when(couponRepository.findById("1")).thenThrow(new RuntimeException("DB error"));

        assertThrows(ServiceException.class, () -> couponService.deleteCoupon("1"));
    }

    @Test
    void testApplyCouponToCart_CartWise() throws ResourceNotFoundException, ServiceException {
        Cart cart = new Cart();
        CartItem cartItem = new CartItem();
        cartItem.setProductId(1);
        cartItem.setQuantity(2);
        cartItem.setPrice(50);

        cart.setItems(List.of(cartItem));

        when(couponRepository.findById("1")).thenReturn(Optional.of(sampleCoupon));

        UpdatedCart updatedCart = couponService.applyCouponToCart("1", cart);

        assertNotNull(updatedCart);
        assertEquals(100, updatedCart.getTotalPrice());
        verify(couponRepository, times(1)).findById("1");
    }
}
