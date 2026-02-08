package com.php.design_patten_demo.pattern.state;

import com.php.design_patten_demo.entity.Cart;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EmptyCartState implements CartStateBehavior {
    @Override
    public void addItem(Cart cart) {
        log.info("空购物车添加商品: userId={}, productId={}", cart.getUserId(), cart.getProductId());
    }

    @Override
    public void removeItem(Cart cart) {
        log.warn("空购物车无法移除商品: userId={}", cart.getUserId());
        throw new IllegalStateException("购物车为空，无法移除商品");
    }

    @Override
    public void checkout(Cart cart) {
        log.warn("空购物车无法结算: userId={}", cart.getUserId());
        throw new IllegalStateException("购物车为空，无法结算");
    }

    @Override
    public boolean canCheckout(Cart cart) {
        return false;
    }
}
