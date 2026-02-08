package com.php.design_patten_demo.controller;

import com.php.design_patten_demo.entity.Cart;
import com.php.design_patten_demo.entity.Order;
import com.php.design_patten_demo.entity.Product;
import com.php.design_patten_demo.entity.User;
import com.php.design_patten_demo.service.CartService;
import com.php.design_patten_demo.service.OrderService;
import com.php.design_patten_demo.service.ProductService;
import com.php.design_patten_demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @GetMapping("/orders")
    public String ordersPage(@RequestParam Long userId, Model model) {
        List<Order> orders = orderService.findByUserId(userId);
        model.addAttribute("orders", orders);
        model.addAttribute("userId", userId);
        return "order/list";
    }

    @GetMapping("/order/create")
    public String createOrderPage(@RequestParam Long userId, Model model) {
        model.addAttribute("userId", userId);
        return "order/create";
    }

    @GetMapping("/api/orders")
    @ResponseBody
    public ResponseEntity<List<Order>> getOrders(@RequestParam Long userId) {
        return ResponseEntity.ok(orderService.findByUserIdOrderByCreatedAtDesc(userId));
    }

    @GetMapping("/api/orders/{id}")
    @ResponseBody
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return orderService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/api/orders")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> createOrder(@RequestParam Long userId, 
                                                            @RequestParam String shippingAddress) {
        try {
            User user = userService.findById(userId);
            if (user == null) {
                Map<String, Object> result = new HashMap<>();
                result.put("success", false);
                result.put("message", "用户不存在");
                return ResponseEntity.badRequest().body(result);
            }

            List<Cart> cartItems = cartService.findByUserId(userId);
            if (cartItems.isEmpty()) {
                Map<String, Object> result = new HashMap<>();
                result.put("success", false);
                result.put("message", "购物车为空");
                return ResponseEntity.badRequest().body(result);
            }

            BigDecimal totalAmount = BigDecimal.ZERO;
            for (Cart cart : cartItems) {
                Product product = productService.findById(cart.getProductId());
                if (product != null) {
                    totalAmount = totalAmount.add(product.getPrice().multiply(new BigDecimal(cart.getQuantity())));
                }
            }

            BigDecimal currentBalance = user.getBalance() != null ? user.getBalance() : BigDecimal.ZERO;
            if (currentBalance.compareTo(totalAmount) < 0) {
                BigDecimal shortage = totalAmount.subtract(currentBalance);
                Map<String, Object> result = new HashMap<>();
                result.put("success", false);
                result.put("needRecharge", true);
                result.put("currentBalance", currentBalance);
                result.put("shortage", shortage);
                result.put("message", "余额不足，当前余额: " + currentBalance + "元，需要: " + totalAmount + "元");
                return ResponseEntity.badRequest().body(result);
            }

            Order order = orderService.createOrder(userId, shippingAddress);
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "订单创建成功");
            result.put("order", order);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }

    @PutMapping("/api/orders/{id}/status")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateOrderStatus(@PathVariable Long id, 
                                                                   @RequestParam String status) {
        Order order = orderService.updateStatus(id, status);
        Map<String, Object> result = new HashMap<>();
        if (order != null) {
            result.put("success", true);
            result.put("message", "状态更新成功");
            result.put("order", order);
        } else {
            result.put("success", false);
            result.put("message", "订单不存在");
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping("/api/orders/{id}/cancel")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> cancelOrder(@PathVariable Long id) {
        try {
            Order order = orderService.cancelOrder(id);
            Map<String, Object> result = new HashMap<>();
            if (order != null) {
                result.put("success", true);
                result.put("message", "订单取消成功");
                result.put("order", order);
            } else {
                result.put("success", false);
                result.put("message", "订单不存在");
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }
}
