package com.monkcommerce.coupons.strategy;

import com.monkcommerce.coupons.dto.CartDTO;
import com.monkcommerce.coupons.dto.CartItemDTO;
import com.monkcommerce.coupons.model.Coupon;
import com.monkcommerce.coupons.model.CouponType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BxGyCouponStrategyTest {
    @Test
    public void testBxGy() {
        BxGyCouponStrategy s = new BxGyCouponStrategy();
        CartDTO cart = new CartDTO();
        cart.getItems().add(new CartItemDTO(1L, 4, 50)); // buy product 1 x4
        cart.getItems().add(new CartItemDTO(3L, 2, 25)); // get product 3 x2
        Coupon c = new Coupon();
        c.setType(CouponType.BXGY);
        c.setDetailsJson("{\"buy_products\":[{\"product_id\":1,\"quantity\":2}], \"get_products\":[{\"product_id\":3,\"quantity\":1}], \"repetition_limit\":2}");
        double d = s.calculateDiscount(cart, c);
        // buy 4 of product 1 => applies 2 times => get up to 2 of product 3 free (each 25) => discount 50
        assertEquals(50.0, d, 0.001);
    }
}
