package com.php.design_patten_demo.pattern.strategy;

import com.php.design_patten_demo.entity.Order;
import com.php.design_patten_demo.enums.OrderStatus;
import com.php.design_patten_demo.service.NotificationService;
import com.php.design_patten_demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PendingStrategy implements OrderStatusStrategy {

    private final UserService userService;
    private final NotificationService notificationService;

    public PendingStrategy(UserService userService, NotificationService notificationService) {
        this.userService = userService;
        this.notificationService = notificationService;
    }

    @Override
    public void process(Order order, OrderStatus oldStatus) {
        if (oldStatus == OrderStatus.PENDING && order.getStatus() == OrderStatus.CANCELLED) {
            userService.recharge(order.getUserId(), order.getTotalAmount());
            notificationService.createNotification(order.getUserId(), "ORDER", "订单已取消", 
                "您的订单 " + order.getOrderNo() + " 已被取消，订单金额 " + order.getTotalAmount() + "元 已退还到您的账户", order.getId());
        }
        log.info("订单 {} 状态: 待处理", order.getOrderNo());
    }
}
