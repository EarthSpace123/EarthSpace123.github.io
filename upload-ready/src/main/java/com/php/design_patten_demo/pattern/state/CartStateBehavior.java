package com.php.design_patten_demo.pattern.state;

import com.php.design_patten_demo.entity.Cart;

public interface CartStateBehavior {
    void addItem(Cart cart);
    void removeItem(Cart cart);
    void checkout(Cart cart);
    boolean canCheckout(Cart cart);
}
