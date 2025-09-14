package com.monkcommerce.coupons.controller;

import com.monkcommerce.coupons.dto.CartDTO;
import com.monkcommerce.coupons.dto.CartItemDTO;
import com.monkcommerce.coupons.model.Coupon;
import com.monkcommerce.coupons.model.CouponType;
import com.monkcommerce.coupons.repository.CouponRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class CouponControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private CouponRepository repo;

    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        repo.deleteAll();
        Coupon c = new Coupon();
        c.setType(CouponType.CART_WISE);
        c.setDetailsJson("{\"threshold\":100, \"discount\":10}");
        repo.save(c);
    }

    @Test
    public void testApplicableCouponsEndpoint() throws Exception {
        CartDTO cart = new CartDTO();
        cart.getItems().add(new CartItemDTO(1L,2,60));
        mvc.perform(post("/api/coupons/applicable-coupons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(cart)))
                .andExpect(status().isOk());
    }
}
