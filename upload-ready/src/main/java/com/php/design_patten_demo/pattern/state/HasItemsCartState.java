package com.php.design_patten_demo.pattern.state;

import com.php.design_patten_demo.entity.Cart;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HasItemsCartState implements CartStateBehavior {
    @Override
    public void addItem(Cart cart) {
        log.info("购物车添加商品: userId={}, productId={}, quantity={}", 
            cart.getUserId(), cart.getProductId(), cart.getQuantity());
    }

    @Override
    public void removeItem(Cart cart) {
        log.info("购物车移除商品: userId={}, productId={}", cart.getUserId(), cart.getProductId());
    }

    @Override
    public void checkout(Cart cart) {
        log.info("购物车结算: userId={}", cart.getUserId());
    }

    @Override
    public boolean canCheckout(Cart cart) {
        return true;
    }
}
