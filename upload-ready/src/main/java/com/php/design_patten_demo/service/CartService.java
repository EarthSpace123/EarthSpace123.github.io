package com.php.design_patten_demo.service;

import com.php.design_patten_demo.entity.Cart;

import java.util.List;
import java.util.Optional;

public interface CartService {
    List<Cart> findByUserId(Long userId);
    Optional<Cart> findByUserIdAndProductId(Long userId, Long productId);
    Optional<Cart> findById(Long cartItemId);
    Cart addToCart(Long userId, Long productId, Integer quantity);
    Cart updateQuantity(Long userId, Long productId, Integer quantity);
    Cart updateQuantityById(Long cartItemId, Integer quantity);
    void removeFromCart(Long userId, Long productId);
    void removeFromCartById(Long cartItemId);
    void clearCart(Long userId);
    boolean canCheckout(Long userId);
    void checkout(Long userId);
}