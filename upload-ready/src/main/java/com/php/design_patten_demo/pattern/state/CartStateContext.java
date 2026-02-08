package com.php.design_patten_demo.pattern.state;

import com.php.design_patten_demo.entity.Cart;
import com.php.design_patten_demo.enums.CartState;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CartStateContext {
    private CartStateBehavior emptyState;
    private CartStateBehavior hasItemsState;
    private CartStateBehavior checkedOutState;

    public CartStateContext() {
        this.emptyState = new EmptyCartState();
        this.hasItemsState = new HasItemsCartState();
        this.checkedOutState = new CheckedOutCartState();
    }

    public CartStateBehavior getState(CartState state) {
        switch (state) {
            case EMPTY:
                return emptyState;
            case HAS_ITEMS:
                return hasItemsState;
            case CHECKED_OUT:
                return checkedOutState;
            default:
                return emptyState;
        }
    }

    public CartStateBehavior getCurrentState(List<Cart> cartItems) {
        if (cartItems == null || cartItems.isEmpty()) {
            return emptyState;
        }
        return hasItemsState;
    }

    public CartStateBehavior getCheckedOutState() {
        return checkedOutState;
    }

    public void addItem(Cart cart, CartStateBehavior currentState) {
        currentState.addItem(cart);
    }

    public void removeItem(Cart cart, CartStateBehavior currentState) {
        currentState.removeItem(cart);
    }

    public void checkout(Cart cart, CartStateBehavior currentState) {
        currentState.checkout(cart);
    }

    public boolean canCheckout(Cart cart, CartStateBehavior currentState) {
        return currentState.canCheckout(cart);
    }
}
