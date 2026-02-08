package com.php.design_patten_demo.controller;

import com.php.design_patten_demo.entity.Notification;
import com.php.design_patten_demo.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/notifications")
    public String notificationsPage(@RequestParam Long userId, Model model) {
        List<Notification> notifications = notificationService.findByUserId(userId);
        model.addAttribute("notifications", notifications);
        model.addAttribute("userId", userId);
        return "notification/list";
    }

    @GetMapping("/api/notifications")
    @ResponseBody
    public ResponseEntity<List<Notification>> getNotifications(@RequestParam Long userId) {
        return ResponseEntity.ok(notificationService.findByUserId(userId));
    }

    @GetMapping("/api/notifications/unread")
    @ResponseBody
    public ResponseEntity<List<Notification>> getUnreadNotifications(@RequestParam Long userId) {
        return ResponseEntity.ok(notificationService.findUnreadByUserId(userId));
    }

    @PutMapping("/api/notifications/{id}/read")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "已标记为已读");
        return ResponseEntity.ok(result);
    }

    @PutMapping("/api/notifications/read-all")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> markAllAsRead(@RequestParam Long userId) {
        notificationService.markAllAsRead(userId);
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "全部已标记为已读");
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/api/notifications/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "删除成功");
        return ResponseEntity.ok(result);
    }
}
