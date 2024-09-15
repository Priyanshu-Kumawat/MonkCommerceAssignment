package com.couponSystem.couponSystem.services;

import com.couponSystem.couponSystem.Exceptions.CouponNotFoundException;
import com.couponSystem.couponSystem.Exceptions.ResourceNotFoundException;
import com.couponSystem.couponSystem.Exceptions.ServiceException;
import com.couponSystem.couponSystem.entity.*;
import com.couponSystem.couponSystem.repositories.CouponRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CouponServiceImpl implements CouponService {


    private final CouponRepository couponRepository;

    public CouponServiceImpl(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }


    @Override
    public Coupon createCoupon(Coupon coupon) throws ServiceException {
        try {
            Coupon savedCoupon = couponRepository.save(coupon);
            return savedCoupon;
        } catch (Exception e) {
            throw new ServiceException("Failed to create the coupon: ");
        }
    }

    @Override
    public List<Coupon> getAllCoupons() throws ServiceException {
        try {
            return couponRepository.findAll();
        } catch (Exception e) {
            throw new ServiceException("Error retrieving all coupons");
        }
    }

    @Override
    public Coupon getCouponById(String id) throws ResourceNotFoundException, ServiceException {
        try {
            Optional<Coupon> coupon = couponRepository.findById(id);
            return coupon.orElseThrow(() -> new ResourceNotFoundException("Coupon not found with id: " + id));
        } catch (Exception e) {
            throw new ServiceException("Error retrieving coupon with id: " + id);
        }
    }


    @Override
    public Coupon updateCoupon(String id, Coupon updatedCoupon) {

        Optional<Coupon> existingCouponOpt = couponRepository.findById(id);

        if (existingCouponOpt.isPresent()) {
            Coupon existingCoupon = existingCouponOpt.get();
            existingCoupon.setType(updatedCoupon.getType());
            existingCoupon.setDetails(updatedCoupon.getDetails());
            return couponRepository.save(existingCoupon);
        } else {
            throw new CouponNotFoundException("Coupon not found with id: " + id);
        }
    }

    @Override
    public void deleteCoupon(String id) throws ResourceNotFoundException, ServiceException {
        try {
            Coupon coupon = getCouponById(id);
            couponRepository.delete(coupon);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Coupon not found with id: " + id);
        } catch (Exception e) {
            throw new ServiceException("Error deleting coupon with id: " + id);
        }
    }

    @Override
    public List<ApplicableCoupon> getApplicableCoupons(Cart cart) throws ServiceException {
        try {
            List<ApplicableCoupon> applicableCoupons = new ArrayList<>();
            List<Coupon> allCoupons = couponRepository.findAll();

            Map<Long, Integer> productQuantities = cart.getItems().stream()
                    .collect(Collectors.toMap(
                            CartItem::getProductId,
                            CartItem::getQuantity,
                            Integer::sum
                    ));

            // Calculate cart total
            double cartTotal = cart.getItems().stream()
                    .mapToDouble(item -> item.getQuantity() * item.getPrice())
                    .sum();

            // Filter Applicable Coupons
            for (Coupon coupon : allCoupons) {
                switch (coupon.getType()) {
                    case CART_WISE:
                        applicableCoupons.addAll(handleCartWiseCoupon(coupon, cartTotal));
                        break;
                    case PRODUCT_WISE:
                        applicableCoupons.addAll(handleProductWiseCoupon(coupon, cart.getItems()));
                        break;
                    case BXGY:
                        applicableCoupons.addAll(handleBxGyCoupon(coupon, productQuantities));
                        break;
                    default:
                        throw new ServiceException("Unknown coupon type: " + coupon.getType());
                }
            }

            return applicableCoupons;
        } catch (Exception e) {
            throw new ServiceException("Error retrieving applicable coupons", e);
        }
    }



    @Override
    public UpdatedCart applyCouponToCart(String couponId, Cart cart) throws ResourceNotFoundException, ServiceException {
        try {
            Coupon coupon = getCouponById(couponId);

            UpdatedCart updatedCart = new UpdatedCart();
            List<UpdatedCartItem> updatedItems = new ArrayList<>();


            double totalPrice = 0;
            for (CartItem item : cart.getItems()) {
                totalPrice += item.getPrice() * item.getQuantity();
            }

            double totalCartDiscount = 0;
            switch (coupon.getType()) {
                case CART_WISE:
                    CartWiseCouponDetails CartWiseCouponDetails = (CartWiseCouponDetails) coupon.getDetails();
                    if (totalPrice >= CartWiseCouponDetails.getThreshold()) {
                        totalCartDiscount = totalPrice * CartWiseCouponDetails.getDiscount() / 100;

                        for (CartItem item : cart.getItems()) {
                            double itemTotalPrice = item.getPrice() * item.getQuantity();
                            double itemContributionPercentage = itemTotalPrice / totalPrice;
                            double itemDiscount = totalCartDiscount * itemContributionPercentage;

                            UpdatedCartItem updatedItem = new UpdatedCartItem(item, itemDiscount);
                            updatedItems.add(updatedItem);
                        }
                    }
                    break;
                case PRODUCT_WISE:
                    ProductWiseCouponDetails ProductWiseCouponDetails = (ProductWiseCouponDetails) coupon.getDetails();
                    for (CartItem item : cart.getItems()) {
                        if (item.getProductId() == ProductWiseCouponDetails.getProductId()) {
                            double discountOnItem = item.getPrice() * item.getQuantity() * ProductWiseCouponDetails.getDiscount() / 100;
                            totalCartDiscount += discountOnItem;
                            updatedItems.add(new UpdatedCartItem(item, discountOnItem));
                        } else {
                            updatedItems.add(new UpdatedCartItem(item, 0));
                        }
                    }
                    break;
                case BXGY:
                    BxGyCouponDetails bxgyDetails = (BxGyCouponDetails) coupon.getDetails();
                    int repetitions = Integer.MAX_VALUE;

                    for (ProductQuantity buyProduct : bxgyDetails.getBuyProducts()) {
                        CartItem cartItem = cart.getItems().stream()
                                .filter(item -> item.getProductId() == buyProduct.getProductId())
                                .findFirst()
                                .orElse(null);

                        if (cartItem != null) {
                            repetitions = Math.min(repetitions, cartItem.getQuantity() / buyProduct.getQuantity());
                        } else {
                            repetitions = 0;
                        }
                    }


                    repetitions = Math.min(repetitions, bxgyDetails.getRepetitionLimit());

                    for (ProductQuantity getProduct : bxgyDetails.getGetProducts()) {
                        CartItem cartItem = cart.getItems().stream()
                                .filter(item -> item.getProductId() == getProduct.getProductId())
                                .findFirst()
                                .orElse(null);

                        if (cartItem != null) {
                            int freeQuantity = Math.min(repetitions * getProduct.getQuantity(), cartItem.getQuantity());
                            double discountOnItem = freeQuantity * cartItem.getPrice();
                            totalCartDiscount += discountOnItem;

                            UpdatedCartItem updatedItem = new UpdatedCartItem(cartItem, discountOnItem);
                            updatedItems.add(updatedItem);
                        }
                    }


                    for (CartItem item : cart.getItems()) {
                        boolean isInGetArray = updatedItems.stream()
                                .anyMatch(updatedItem -> updatedItem.getProductId() == item.getProductId());

                        if (!isInGetArray) {
                            UpdatedCartItem updatedItem = new UpdatedCartItem(item, 0.0);
                            updatedItems.add(updatedItem);
                        }
                    }

                    break;
            }

            updatedCart.setUpdatedItems(updatedItems);
            updatedCart.setTotalPrice(totalPrice);
            updatedCart.setTotalDiscount(totalCartDiscount);
            updatedCart.setFinalPrice(totalPrice - totalCartDiscount);


            return updatedCart;
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Coupon not found with id: " + couponId);
        } catch (Exception e) {
            throw new ServiceException("Error applying coupon with id: " + couponId);
        }
    }


    private List<ApplicableCoupon> handleCartWiseCoupon(Coupon coupon, double cartTotal) {
        List<ApplicableCoupon> applicableCoupons = new ArrayList<>();
        CartWiseCouponDetails details = (CartWiseCouponDetails) coupon.getDetails();
        if (cartTotal > details.getThreshold()) {
            double discount = (cartTotal * details.getDiscount()) / 100;
            ApplicableCoupon applicableCoupon = new ApplicableCoupon(coupon.getId(), coupon.getType(), discount);
            applicableCoupons.add(applicableCoupon);
        }
        return applicableCoupons;
    }

    private List<ApplicableCoupon> handleProductWiseCoupon(Coupon coupon, List<CartItem> cartItems) {
        List<ApplicableCoupon> applicableCoupons = new ArrayList<>();
        ProductWiseCouponDetails details = (ProductWiseCouponDetails) coupon.getDetails();
        for (CartItem item : cartItems) {
            if (item.getProductId() == details.getProductId()) {
                double discount = (item.getQuantity() * item.getPrice() * details.getDiscount()) / 100;
                ApplicableCoupon applicableCoupon = new ApplicableCoupon(coupon.getId(), coupon.getType(), discount);
                applicableCoupons.add(applicableCoupon);
                break;
            }
        }
        return applicableCoupons;
    }

    private List<ApplicableCoupon> handleBxGyCoupon(Coupon coupon, Map<Long, Integer> productQuantities) {
        List<ApplicableCoupon> applicableCoupons = new ArrayList<>();
        BxGyCouponDetails details = (BxGyCouponDetails) coupon.getDetails();

        int buyCount = details.getBuyProducts().stream()
                .mapToInt(buyProduct -> productQuantities.getOrDefault(buyProduct.getProductId(), 0) / buyProduct.getQuantity())
                .min()
                .orElse(0);

        int repetitionLimit = details.getRepetitionLimit();
        if (buyCount > 0) {
            int effectiveRepetition = Math.min(buyCount, repetitionLimit);
            double totalDiscount = effectiveRepetition * details.getGetProducts().size() * details.getGetProducts().get(0).getQuantity() * 25;  // Assuming price of free items is 25
            ApplicableCoupon applicableCoupon = new ApplicableCoupon(coupon.getId(), coupon.getType(), totalDiscount);
            applicableCoupons.add(applicableCoupon);
        }
        return applicableCoupons;
    }

}
