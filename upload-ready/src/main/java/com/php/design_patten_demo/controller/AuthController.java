package com.php.design_patten_demo.controller;

import com.php.design_patten_demo.dto.LoginRequest;
import com.php.design_patten_demo.entity.User;
import com.php.design_patten_demo.enums.UserRole;
import com.php.design_patten_demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }



    @PostMapping("/api/auth/login")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest loginRequest) {
        User user = userService.findByUsername(loginRequest.getUsername());
        Map<String, Object> result = new HashMap<>();

        if (user == null) {
            result.put("success", false);
            result.put("message", "用户不存在");
            return ResponseEntity.badRequest().body(result);
        }

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            result.put("success", false);
            result.put("message", "密码错误");
            return ResponseEntity.badRequest().body(result);
        }

        if (loginRequest.getRole() != null && !loginRequest.getRole().isEmpty()) {
            if (!user.getRole().name().equals(loginRequest.getRole())) {
                result.put("success", false);
                result.put("message", "权限等级不匹配，您的权限是：" + user.getRole().name());
                return ResponseEntity.badRequest().body(result);
            }
        }

        result.put("success", true);
        result.put("message", "登录成功");
        result.put("user", user);
        return ResponseEntity.ok(result);
    }



    @GetMapping("/api/auth/current")
    @ResponseBody
    public ResponseEntity<User> getCurrentUser(@RequestParam Long userId) {
        User user = userService.findById(userId);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/api/auth/logout")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> logout() {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "登出成功");
        return ResponseEntity.ok(result);
    }
}
