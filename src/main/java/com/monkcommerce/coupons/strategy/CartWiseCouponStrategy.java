package com.monkcommerce.coupons.strategy;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.monkcommerce.coupons.dto.CartDTO;
import com.monkcommerce.coupons.model.Coupon;
import org.springframework.stereotype.Component;

@Component("CART_WISE")
public class CartWiseCouponStrategy implements CouponStrategy {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public double calculateDiscount(CartDTO cart, Coupon coupon) {
        try {
            JsonNode node = mapper.readTree(coupon.getDetailsJson());
            double threshold = node.path("threshold").asDouble(0);
            double discountPercent = node.path("discount").asDouble(0);
            double total = cart.total();
            if (total > threshold) {
                return total * (discountPercent / 100.0);
            }
        } catch (Exception ignored) {}
        return 0;
    }

    @Override
    public CartDTO apply(CartDTO cart, Coupon coupon) {
        return cart;
    }
}
