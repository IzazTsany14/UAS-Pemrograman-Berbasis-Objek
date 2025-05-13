package com.zayyani.cafe.service;

import com.zayyani.cafe.model.Cart;
import com.zayyani.cafe.model.Order;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {
    private final List<Order> orders = new ArrayList<>();
    
    public Order createOrder(Cart cart, String orderType, String paymentMethod, String kasir) {
        String orderId = generateOrderId();
        Order order = new Order(orderId, new ArrayList<>(cart.getItems()), orderType, paymentMethod, kasir);
        orders.add(order);
        return order;
    }
    
    public void processPayment(Order order, double amountPaid) {
        order.setAmountPaid(amountPaid);
        order.setChange(amountPaid - order.getTotal());
    }
    
    public List<Order> getAllOrders() {
        return orders;
    }
    
    public Order getOrder(String id) {
        return orders.stream()
                .filter(order -> order.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
    
    private String generateOrderId() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timestamp = now.format(formatter);
        String randomPart = UUID.randomUUID().toString().substring(0, 4);
        return "ORD-" + timestamp + "-" + randomPart;
    }
}