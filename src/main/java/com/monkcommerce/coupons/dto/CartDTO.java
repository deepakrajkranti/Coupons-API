package com.monkcommerce.coupons.dto;

import java.util.ArrayList;
import java.util.List;

public class CartDTO {
    private List<CartItemDTO> items = new ArrayList<>();

    public CartDTO() {}
    public List<CartItemDTO> getItems() { return items; }
    public void setItems(List<CartItemDTO> items) { this.items = items; }

    public double total() {

        return items.stream().mapToDouble(i -> i.getPrice() * i.getQuantity()).sum();
    }
}
