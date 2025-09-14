package com.monkcommerce.coupons.strategy;

import com.monkcommerce.coupons.dto.CartDTO;
import com.monkcommerce.coupons.dto.CartItemDTO;
import com.monkcommerce.coupons.model.Coupon;
import com.monkcommerce.coupons.model.CouponType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CartWiseCouponStrategyTest {
    @Test
    public void testCalculateDiscount() {
        CartWiseCouponStrategy s = new CartWiseCouponStrategy();
        CartDTO cart = new CartDTO();
        cart.getItems().add(new CartItemDTO(1L, 2, 60)); // total 120
        Coupon c = new Coupon();
        c.setType(CouponType.CART_WISE);
        c.setDetailsJson("{\"threshold\":100, \"discount\":10}");
        double d = s.calculateDiscount(cart, c);
        assertEquals(12.0, d, 0.001);
    }
}
