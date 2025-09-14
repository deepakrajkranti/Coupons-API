package com.monkcommerce.coupons.strategy;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.monkcommerce.coupons.dto.CartDTO;
import com.monkcommerce.coupons.model.Coupon;
import org.springframework.stereotype.Component;

@Component("PRODUCT_WISE")
public class ProductWiseCouponStrategy implements CouponStrategy {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public double calculateDiscount(CartDTO cart, Coupon coupon) {
        try {
            JsonNode node = mapper.readTree(coupon.getDetailsJson());
            long productId = node.path("product_id").asLong(-1);
            double discountPercent = node.path("discount").asDouble(0);
            return cart.getItems().stream()
                    .filter(i -> i.getProductId() == productId)
                    .mapToDouble(i -> i.getPrice() * i.getQuantity() * (discountPercent / 100.0))
                    .sum();
        } catch (Exception ignored) {

        }
        return 0;
    }

    @Override
    public CartDTO apply(CartDTO cart, Coupon coupon) {
        return cart;
    }
}
