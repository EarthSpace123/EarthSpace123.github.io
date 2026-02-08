package com.php.design_patten_demo.controller;

import com.php.design_patten_demo.entity.User;
import com.php.design_patten_demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Controller
public class ProfileController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public String profilePage(@RequestParam Long userId, Model model) {
        User user = userService.findById(userId);
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        model.addAttribute("userId", userId);
        return "profile/index";
    }

    @GetMapping("/api/profile")
    @ResponseBody
    public ResponseEntity<User> getProfile(@RequestParam Long userId) {
        User user = userService.findById(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping("/api/profile/recharge")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> recharge(@RequestParam Long userId, 
                                                        @RequestParam BigDecimal amount) {
        try {
            User user = userService.recharge(userId, amount);
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "充值成功");
            result.put("balance", user.getBalance());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }

    @PutMapping("/api/profile/update")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateProfile(@RequestParam Long userId,
                                                             @RequestParam(required = false) String email,
                                                             @RequestParam(required = false) String phone) {
        try {
            User user = userService.findById(userId);
            if (user == null) {
                Map<String, Object> result = new HashMap<>();
                result.put("success", false);
                result.put("message", "用户不存在");
                return ResponseEntity.notFound().build();
            }
            
            if (email != null && !email.isEmpty()) {
                user.setEmail(email);
            }
            if (phone != null && !phone.isEmpty()) {
                user.setPhone(phone);
            }
            
            User updatedUser = userService.updateUser(user);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "个人信息更新成功");
            result.put("user", updatedUser);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }
}
