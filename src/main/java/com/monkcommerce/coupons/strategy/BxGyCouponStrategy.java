package com.monkcommerce.coupons.strategy;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.monkcommerce.coupons.dto.CartDTO;
import com.monkcommerce.coupons.dto.CartItemDTO;
import com.monkcommerce.coupons.model.Coupon;
import org.springframework.stereotype.Component;

import java.util.*;

@Component("BXGY")
public class BxGyCouponStrategy implements CouponStrategy {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public double calculateDiscount(CartDTO cart, Coupon coupon) {
        try {
            JsonNode root = mapper.readTree(coupon.getDetailsJson());
            JsonNode buyArray = root.path("buy_products");
            JsonNode getArray = root.path("get_products");
            int repetitionLimit = root.path("repetition_limit").asInt(1);

            Map<Long, Integer> buyReq = new HashMap<>();
            for (JsonNode bn : buyArray) {
                long pid = bn.path("product_id").asLong();
                int qty = bn.path("quantity").asInt(1);
                buyReq.put(pid, buyReq.getOrDefault(pid, 0) + qty);
            }

            Map<Long, Integer> getReq = new HashMap<>();
            for (JsonNode gn : getArray) {
                long pid = gn.path("product_id").asLong();
                int qty = gn.path("quantity").asInt(1);
                getReq.put(pid, getReq.getOrDefault(pid, 0) + qty);
            }

            Map<Long, Integer> cartMap = new HashMap<>();
            Map<Long, Double> priceMap = new HashMap<>();
            for (CartItemDTO item : cart.getItems()) {
                cartMap.put(item.getProductId(), item.getQuantity());
                priceMap.put(item.getProductId(), item.getPrice());
            }

            int times = Integer.MAX_VALUE;
            for (Map.Entry<Long, Integer> entry : buyReq.entrySet()) {
                long pid = entry.getKey();
                int reqQty = entry.getValue();
                int have = cartMap.getOrDefault(pid, 0);
                if (have == 0) { times = 0; break; }
                times = Math.min(times, have / reqQty);
            }

            if (times == 0) return 0;
            times = Math.min(times, repetitionLimit);

            double discount = 0.0;
            for (Map.Entry<Long, Integer> g : getReq.entrySet()) {
                long getPid = g.getKey();
                int getQtyPer = g.getValue();
                int availableInCart = cartMap.getOrDefault(getPid, 0);
                int freeCount = Math.min(availableInCart, getQtyPer * times);
                discount += freeCount * priceMap.getOrDefault(getPid, 0.0);
            }

            return discount;

        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public CartDTO apply(CartDTO cart, Coupon coupon) {
        return cart;
    }
}
