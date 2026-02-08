package com.php.design_patten_demo.pattern.strategy;

import com.php.design_patten_demo.entity.Order;
import com.php.design_patten_demo.enums.OrderStatus;
import com.php.design_patten_demo.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ShippedStrategy implements OrderStatusStrategy {

    private final NotificationService notificationService;

    public ShippedStrategy(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public void process(Order order, OrderStatus oldStatus) {
        if (order.getStatus() == OrderStatus.SHIPPED) {
            notificationService.createNotification(order.getUserId(), "ORDER", "订单已发货", 
                "您的订单 " + order.getOrderNo() + " 已发货，请耐心等待收货", order.getId());
        }
        log.info("订单 {} 状态: 已发货", order.getOrderNo());
    }
}
