package com.monkcommerce.coupons.controller;

import com.monkcommerce.coupons.dto.CartDTO;
import com.monkcommerce.coupons.model.Coupon;
import com.monkcommerce.coupons.service.CouponService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/coupons")
public class CouponController {
    private final CouponService service;

    public CouponController(CouponService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<Coupon> create(@RequestBody Coupon coupon) {
        return ResponseEntity.ok(service.create(coupon));
    }

    @GetMapping
    public ResponseEntity<List<Coupon>> all() { return ResponseEntity.ok(service.getAll()); }

    @GetMapping("/{id}")
    public ResponseEntity<Coupon> get(@PathVariable Long id) { return ResponseEntity.ok(service.getById(id)); }

    @PutMapping("/{id}")
    public ResponseEntity<Coupon> update(@PathVariable Long id, @RequestBody Coupon coupon) {
        return ResponseEntity.ok(service.update(id, coupon));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) { service.delete(id); return ResponseEntity.noContent().build(); }

    @PostMapping("/calculate/{id}")
    public ResponseEntity<Map<String, Object>> calculate(@PathVariable Long id, @RequestBody CartDTO cart) {
        double discount = service.calculateDiscountForCoupon(id, cart);
        Map<String, Object> res = new HashMap<>();
        res.put("coupon_id", id);
        res.put("discount", discount);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/applicable-coupons")
    public ResponseEntity<Map<String, Object>> applicableCoupons(@RequestBody CartDTO cart) {
        List<Coupon> coupons = service.getAll();
        Map<String, Object> res = new HashMap<>();
        res.put("applicable_coupons", coupons.stream().map(c -> {
            double d = 0;
            try {
                d = service.calculateDiscountForCoupon(c.getId(), cart);
            } catch (Exception ignored) {}
            Map<String,Object> m = new HashMap<>();
            m.put("coupon_id", c.getId());
            m.put("type", c.getType());
            m.put("discount", d);
            return m;
        }).toList());
        return ResponseEntity.ok(res);
    }
}
