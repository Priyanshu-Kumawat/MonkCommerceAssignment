package com.couponSystem.couponSystem.repositories;

import com.couponSystem.couponSystem.entity.Coupon;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository extends MongoRepository<Coupon, String> {

}
