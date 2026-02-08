package com.php.design_patten_demo.service;

import com.php.design_patten_demo.entity.Order;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    Order createOrder(Long userId, String shippingAddress);
    List<Order> findByUserId(Long userId);
    List<Order> findByUserIdOrderByCreatedAtDesc(Long userId);
    Optional<Order> findById(Long id);
    Optional<Order> findByOrderNo(String orderNo);
    Order updateStatus(Long orderId, String status);
    Order cancelOrder(Long orderId);
    void deleteOrder(Long id);
}
