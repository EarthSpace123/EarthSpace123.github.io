package com.php.design_patten_demo.service;

import com.php.design_patten_demo.entity.Notification;

import java.util.List;

public interface NotificationService {
    Notification createNotification(Long userId, String type, String title, String content, Long relatedId);
    List<Notification> findByUserId(Long userId);
    List<Notification> findUnreadByUserId(Long userId);
    void markAsRead(Long notificationId);
    void markAllAsRead(Long userId);
    void deleteNotification(Long id);
}
