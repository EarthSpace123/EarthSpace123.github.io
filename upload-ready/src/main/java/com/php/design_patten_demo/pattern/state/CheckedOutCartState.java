package com.php.design_patten_demo.pattern.state;

import com.php.design_patten_demo.entity.Cart;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CheckedOutCartState implements CartStateBehavior {
    @Override
    public void addItem(Cart cart) {
        log.warn("已结算购物车无法添加商品: userId={}", cart.getUserId());
        throw new IllegalStateException("购物车已结算，无法添加商品");
    }

    @Override
    public void removeItem(Cart cart) {
        log.warn("已结算购物车无法移除商品: userId={}", cart.getUserId());
        throw new IllegalStateException("购物车已结算，无法移除商品");
    }

    @Override
    public void checkout(Cart cart) {
        log.warn("已结算购物车无法再次结算: userId={}", cart.getUserId());
        throw new IllegalStateException("购物车已结算，无法再次结算");
    }

    @Override
    public boolean canCheckout(Cart cart) {
        return false;
    }
}
