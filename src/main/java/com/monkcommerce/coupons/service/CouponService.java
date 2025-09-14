package com.monkcommerce.coupons.service;

import com.monkcommerce.coupons.dto.CartDTO;
import com.monkcommerce.coupons.model.Coupon;

import java.util.List;

public interface CouponService {
    Coupon create(Coupon coupon);
    List<Coupon> getAll();
    Coupon getById(Long id);
    Coupon update(Long id, Coupon coupon);
    void delete(Long id);
    double calculateDiscountForCoupon(Long couponId, CartDTO cart);
}
