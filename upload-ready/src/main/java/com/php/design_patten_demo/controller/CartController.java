package com.php.design_patten_demo.controller;

import com.php.design_patten_demo.entity.Cart;
import com.php.design_patten_demo.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/cart")
    public String cartPage(@RequestParam Long userId, Model model) {
        List<Cart> cartItems = cartService.findByUserId(userId);
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("userId", userId);
        return "cart";
    }

    @GetMapping("/api/cart")
    @ResponseBody
    public ResponseEntity<List<Cart>> getCart(@RequestParam Long userId) {
        return ResponseEntity.ok(cartService.findByUserId(userId));
    }

    @PostMapping("/api/cart/add")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> addToCart(@RequestParam Long userId, 
                                                         @RequestParam Long productId, 
                                                         @RequestParam(defaultValue = "1") Integer quantity) {
        Cart cart = cartService.addToCart(userId, productId, quantity);
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "添加到购物车成功");
        result.put("cart", cart);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/api/cart/update")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateQuantity(@RequestParam Long cartItemId, 
                                                               @RequestParam Integer quantity) {
        Cart cart = cartService.updateQuantityById(cartItemId, quantity);
        Map<String, Object> result = new HashMap<>();
        if (cart != null) {
            result.put("success", true);
            result.put("message", "更新成功");
            result.put("cart", cart);
        } else {
            result.put("success", false);
            result.put("message", "更新失败，购物车项不存在");
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping("/api/cart/remove")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> removeFromCartById(@RequestParam Long cartItemId) {
        cartService.removeFromCartById(cartItemId);
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "删除成功");
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/api/cart")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> removeFromCart(@RequestParam Long userId, 
                                                              @RequestParam Long productId) {
        cartService.removeFromCart(userId, productId);
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "删除成功");
        return ResponseEntity.ok(result);
    }

    @PostMapping("/api/cart/can-checkout")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> canCheckout(@RequestParam Long userId) {
        boolean canCheckout = cartService.canCheckout(userId);
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("canCheckout", canCheckout);
        result.put("message", canCheckout ? "可以结算" : "购物车为空，无法结算");
        return ResponseEntity.ok(result);
    }

    @PostMapping("/api/cart/checkout")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> checkout(@RequestParam Long userId) {
        try {
            cartService.checkout(userId);
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "结算成功");
            return ResponseEntity.ok(result);
        } catch (IllegalStateException e) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", e.getMessage());
            return ResponseEntity.ok(result);
        }
    }
}
