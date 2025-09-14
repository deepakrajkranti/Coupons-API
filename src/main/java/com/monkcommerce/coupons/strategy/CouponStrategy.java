package com.monkcommerce.coupons.strategy;

import com.monkcommerce.coupons.dto.CartDTO;
import com.monkcommerce.coupons.model.Coupon;

public interface CouponStrategy {
    double calculateDiscount(CartDTO cart, Coupon coupon);
    CartDTO apply(CartDTO cart, Coupon coupon);
}
