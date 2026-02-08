package com.php.design_patten_demo.repository;

import com.php.design_patten_demo.entity.Order;
import com.php.design_patten_demo.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);
    List<Order> findByUserIdOrderByCreatedAtDesc(Long userId);
    Optional<Order> findByOrderNo(String orderNo);
    List<Order> findByStatus(OrderStatus status);
}
