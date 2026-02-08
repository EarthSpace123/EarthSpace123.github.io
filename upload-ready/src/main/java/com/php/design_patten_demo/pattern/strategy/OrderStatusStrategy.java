package com.php.design_patten_demo.pattern.strategy;

import com.php.design_patten_demo.entity.Order;
import com.php.design_patten_demo.enums.OrderStatus;

public interface OrderStatusStrategy {
    void process(Order order, OrderStatus oldStatus);
}
