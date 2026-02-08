package com.php.design_patten_demo.pattern.strategy;

import com.php.design_patten_demo.entity.Order;
import com.php.design_patten_demo.enums.OrderStatus;
import com.php.design_patten_demo.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CompletedStrategy implements OrderStatusStrategy {

    private final NotificationService notificationService;

    public CompletedStrategy(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public void process(Order order, OrderStatus oldStatus) {
        if (order.getStatus() == OrderStatus.COMPLETED) {
            notificationService.createNotification(order.getUserId(), "ORDER", "订单已完成", 
                "您的订单 " + order.getOrderNo() + " 已完成，感谢您的购买", order.getId());
        }
        log.info("订单 {} 状态: 已完成", order.getOrderNo());
    }
}
