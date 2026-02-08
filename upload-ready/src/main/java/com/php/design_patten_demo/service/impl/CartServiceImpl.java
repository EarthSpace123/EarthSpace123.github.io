package com.php.design_patten_demo.service.impl;

import com.php.design_patten_demo.entity.Cart;
import com.php.design_patten_demo.pattern.state.CartStateContext;
import com.php.design_patten_demo.repository.CartRepository;
import com.php.design_patten_demo.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartStateContext cartStateContext;

    @Override
    public List<Cart> findByUserId(Long userId) {
        return cartRepository.findByUserId(userId);
    }

    @Override
    public Optional<Cart> findByUserIdAndProductId(Long userId, Long productId) {
        return cartRepository.findByUserIdAndProductId(userId, productId);
    }

    @Override
    public Optional<Cart> findById(Long cartItemId) {
        return cartRepository.findById(cartItemId);
    }

    @Override
    public Cart addToCart(Long userId, Long productId, Integer quantity) {
        List<Cart> cartItems = findByUserId(userId);
        var currentState = cartStateContext.getCurrentState(cartItems);
        
        return findByUserIdAndProductId(userId, productId)
            .map(cart -> {
                cart.setQuantity(cart.getQuantity() + quantity);
                cartStateContext.addItem(cart, currentState);
                return cartRepository.save(cart);
            })
            .orElseGet(() -> {
                Cart cart = new Cart();
                cart.setUserId(userId);
                cart.setProductId(productId);
                cart.setQuantity(quantity);
                cartStateContext.addItem(cart, currentState);
                return cartRepository.save(cart);
            });
    }

    @Override
    public Cart updateQuantity(Long userId, Long productId, Integer quantity) {
        return findByUserIdAndProductId(userId, productId)
            .map(cart -> {
                cart.setQuantity(quantity);
                return cartRepository.save(cart);
            })
            .orElse(null);
    }

    @Override
    public Cart updateQuantityById(Long cartItemId, Integer quantity) {
        return findById(cartItemId)
            .map(cart -> {
                cart.setQuantity(quantity);
                return cartRepository.save(cart);
            })
            .orElse(null);
    }

    @Override
    public void removeFromCart(Long userId, Long productId) {
        List<Cart> cartItems = findByUserId(userId);
        var currentState = cartStateContext.getCurrentState(cartItems);
        
        findByUserIdAndProductId(userId, productId).ifPresent(cart -> {
            cartStateContext.removeItem(cart, currentState);
            cartRepository.deleteByUserIdAndProductId(userId, productId);
        });
    }

    @Override
    public void removeFromCartById(Long cartItemId) {
        cartRepository.deleteById(cartItemId);
    }

    @Override
    public void clearCart(Long userId) {
        List<Cart> carts = findByUserId(userId);
        cartRepository.deleteAll(carts);
    }

    @Override
    public boolean canCheckout(Long userId) {
        List<Cart> cartItems = findByUserId(userId);
        var currentState = cartStateContext.getCurrentState(cartItems);
        return cartStateContext.canCheckout(null, currentState);
    }

    @Override
    public void checkout(Long userId) {
        List<Cart> cartItems = findByUserId(userId);
        var currentState = cartStateContext.getCurrentState(cartItems);
        
        if (!cartStateContext.canCheckout(null, currentState)) {
            throw new IllegalStateException("购物车为空，无法结算");
        }
        
        cartItems.forEach(cart -> cartStateContext.checkout(cart, currentState));
        clearCart(userId);
    }
}
