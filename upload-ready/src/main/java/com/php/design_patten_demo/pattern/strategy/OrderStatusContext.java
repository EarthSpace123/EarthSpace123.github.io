package com.php.design_patten_demo.pattern.strategy;

import com.php.design_patten_demo.entity.Order;
import com.php.design_patten_demo.enums.OrderStatus;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrderStatusContext {
    private final Map<OrderStatus, OrderStatusStrategy> strategies = new HashMap<>();

    public OrderStatusContext(PendingStrategy pendingStrategy, 
                            ShippedStrategy shippedStrategy, 
                            CompletedStrategy completedStrategy) {
        strategies.put(OrderStatus.PENDING, pendingStrategy);
        strategies.put(OrderStatus.SHIPPED, shippedStrategy);
        strategies.put(OrderStatus.COMPLETED, completedStrategy);
    }

    public void executeStrategy(Order order, OrderStatus oldStatus) {
        OrderStatusStrategy strategy = strategies.get(order.getStatus());
        if (strategy != null) {
            strategy.process(order, oldStatus);
        }
    }
}
