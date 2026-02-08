package com.php.design_patten_demo.controller;

import com.php.design_patten_demo.entity.User;
import com.php.design_patten_demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/api/users/address")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateAddress(@RequestParam Long userId, 
                                                               @RequestParam String address) {
        try {
            User user = userService.findById(userId);
            if (user == null) {
                Map<String, Object> result = new HashMap<>();
                result.put("success", false);
                result.put("message", "用户不存在");
                return ResponseEntity.badRequest().body(result);
            }

            user.setAddress(address);
            userService.updateUser(user);

            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "地址更新成功");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }
}
