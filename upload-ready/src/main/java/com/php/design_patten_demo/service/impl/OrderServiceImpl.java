package com.php.design_patten_demo.service.impl;

import com.php.design_patten_demo.entity.Cart;
import com.php.design_patten_demo.entity.Order;
import com.php.design_patten_demo.entity.OrderItem;
import com.php.design_patten_demo.entity.Product;
import com.php.design_patten_demo.enums.OrderStatus;
import com.php.design_patten_demo.repository.OrderRepository;
import com.php.design_patten_demo.service.CartService;
import com.php.design_patten_demo.service.NotificationService;
import com.php.design_patten_demo.service.OrderService;
import com.php.design_patten_demo.service.ProductService;
import com.php.design_patten_demo.service.UserService;
import com.php.design_patten_demo.pattern.observer.OrderSubject;
import com.php.design_patten_demo.pattern.strategy.OrderStatusContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private CartService cartService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private OrderSubject orderSubject;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderStatusContext orderStatusContext;

    @Override
    @Transactional
    public Order createOrder(Long userId, String shippingAddress) {
        List<Cart> cartItems = cartService.findByUserId(userId);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("购物车为空");
        }

        Order order = new Order();
        order.setOrderNo(generateOrderNo());
        order.setUserId(userId);
        order.setUser(userService.findById(userId));
        order.setStatus(OrderStatus.PENDING);
        order.setShippingAddress(shippingAddress);

        BigDecimal totalAmount = BigDecimal.ZERO;
        for (Cart cart : cartItems) {
            Product product = productService.findById(cart.getProductId());
            if (product == null || product.getStock() < cart.getQuantity()) {
                throw new RuntimeException("商品库存不足: " + product.getName());
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setProductName(product.getName());
            orderItem.setPrice(product.getPrice());
            orderItem.setQuantity(cart.getQuantity());
            orderItem.setSubtotal(product.getPrice().multiply(new BigDecimal(cart.getQuantity())));

            order.getOrderItems().add(orderItem);
            totalAmount = totalAmount.add(orderItem.getSubtotal());

            productService.updateStock(product.getId(), -cart.getQuantity());
        }

        order.setTotalAmount(totalAmount);
        Order savedOrder = orderRepository.save(order);

        userService.deductBalance(userId, totalAmount);

        cartService.clearCart(userId);

        orderSubject.notify(savedOrder.getOrderNo(), "新订单创建，请及时处理");

        notificationService.createNotification(userId, "ORDER", "订单创建成功", 
            "您的订单 " + savedOrder.getOrderNo() + " 已创建成功，总金额: " + totalAmount + "元", savedOrder.getId());

        return savedOrder;
    }

    @Override
    public List<Order> findByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public List<Order> findByUserIdOrderByCreatedAtDesc(Long userId) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Override
    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    public Optional<Order> findByOrderNo(String orderNo) {
        return orderRepository.findByOrderNo(orderNo);
    }

    @Override
    @Transactional
    public Order updateStatus(Long orderId, String status) {
        return orderRepository.findById(orderId)
            .map(order -> {
                OrderStatus oldStatus = order.getStatus();
                OrderStatus newStatus = OrderStatus.valueOf(status);
                
                order.setStatus(newStatus);
                Order updatedOrder = orderRepository.save(order);

                orderStatusContext.executeStrategy(updatedOrder, oldStatus);

                return updatedOrder;
            })
            .orElse(null);
    }

    @Override
    @Transactional
    public Order cancelOrder(Long orderId) {
        return orderRepository.findById(orderId)
            .map(order -> {
                if (order.getStatus() != OrderStatus.PENDING) {
                    throw new RuntimeException("只有待审批状态的订单才能取消");
                }

                order.setStatus(OrderStatus.CANCELLED);
                Order updatedOrder = orderRepository.save(order);

                userService.recharge(order.getUserId(), order.getTotalAmount());

                notificationService.createNotification(order.getUserId(), "ORDER", "订单已取消", 
                    "您的订单 " + order.getOrderNo() + " 已取消，订单金额 " + order.getTotalAmount() + "元 已退还到您的账户", order.getId());

                return updatedOrder;
            })
            .orElse(null);
    }

    @Override
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    private String generateOrderNo() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String random = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return "ORD" + timestamp + random;
    }
}
