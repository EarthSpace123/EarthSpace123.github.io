package com.php.design_patten_demo.service.impl;

import com.php.design_patten_demo.entity.Notification;
import com.php.design_patten_demo.entity.Order;
import com.php.design_patten_demo.repository.NotificationRepository;
import com.php.design_patten_demo.repository.OrderRepository;
import com.php.design_patten_demo.service.NotificationService;
import com.php.design_patten_demo.pattern.observer.OrderObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService, OrderObserver {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public Notification createNotification(Long userId, String type, String title, String content, Long relatedId) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setType(type);
        notification.setTitle(title);
        notification.setContent(content);
        notification.setRelatedId(relatedId);
        notification.setIsRead(false);
        return notificationRepository.save(notification);
    }

    @Override
    public List<Notification> findByUserId(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Override
    public List<Notification> findUnreadByUserId(Long userId) {
        return notificationRepository.findByUserIdAndIsReadFalse(userId);
    }

    @Override
    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId).orElse(null);
        if (notification != null) {
            notification.setIsRead(true);
            notificationRepository.save(notification);
        }
    }

    @Override
    public void markAllAsRead(Long userId) {
        List<Notification> notifications = findUnreadByUserId(userId);
        notifications.forEach(n -> n.setIsRead(true));
        notificationRepository.saveAll(notifications);
    }

    @Override
    public void deleteNotification(Long id) {
        notificationRepository.deleteById(id);
    }

    @Override
    public void update(String orderNo, String message) {
        Order order = orderRepository.findByOrderNo(orderNo).orElse(null);
        if (order == null) {
            return;
        }
        
        Notification notification = new Notification();
        notification.setUserId(order.getUserId());
        notification.setType("ORDER");
        notification.setTitle("订单状态更新");
        notification.setContent("订单 " + orderNo + ": " + message);
        notification.setIsRead(false);
        notificationRepository.save(notification);
    }
}
