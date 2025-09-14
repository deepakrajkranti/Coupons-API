package com.monkcommerce.coupons.service.impl;

import com.monkcommerce.coupons.dto.CartDTO;
import com.monkcommerce.coupons.model.Coupon;
import com.monkcommerce.coupons.repository.CouponRepository;
import com.monkcommerce.coupons.service.CouponService;
import com.monkcommerce.coupons.strategy.CouponStrategy;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CouponServiceImpl implements CouponService {
    private final CouponRepository repo;
    private final BeanFactory beanFactory;

    private static final Map<String, String> STRATEGY_BEAN = new HashMap<>();
    static {
        STRATEGY_BEAN.put("CART_WISE", "CART_WISE");
        STRATEGY_BEAN.put("PRODUCT_WISE", "PRODUCT_WISE");
        STRATEGY_BEAN.put("BXGY", "BXGY");
    }

    public CouponServiceImpl(CouponRepository repo, BeanFactory beanFactory) {
        this.repo = repo;
        this.beanFactory = beanFactory;
    }

    @Override
    public Coupon create(Coupon coupon) {
        return repo.save(coupon);
    }

    @Override
    public List<Coupon> getAll() {
        return repo.findAll();
    }

    @Override
    public Coupon getById(Long id) {
        return repo.findById(id).
                orElseThrow(() -> new EntityNotFoundException("Coupon not found"));
    }

    @Override
    public Coupon update(Long id, Coupon coupon) {
        Coupon ex = getById(id);
        ex.setDetailsJson(coupon.getDetailsJson());
        ex.setType(coupon.getType());
        ex.setRepetitionLimit(coupon.getRepetitionLimit());
        ex.setExpiryDate(coupon.getExpiryDate());
        return repo.save(ex);
    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }

    @Override
    public double calculateDiscountForCoupon(Long couponId, CartDTO cart) {
        Coupon c = getById(couponId);
        String beanName = STRATEGY_BEAN.get(c.getType().name());
        CouponStrategy strategy = (CouponStrategy) beanFactory.getBean(beanName);
        return strategy.calculateDiscount(cart, c);
    }
}
