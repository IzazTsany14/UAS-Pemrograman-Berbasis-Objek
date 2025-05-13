package com.zayyani.cafe.model;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Order {
    private String id;
    private List<CartItem> items;
    private LocalDateTime orderTime;
    private String orderType; // Dine In or Take Away
    private String paymentMethod;
    private double subtotal;
    private double discount;
    private double total;
    private double amountPaid;
    private double change;
    private String kasir;

    public Order(String id, List<CartItem> items, String orderType, String paymentMethod, String kasir) {
        this.id = id;
        this.items = items;
        this.orderTime = LocalDateTime.now();
        this.orderType = orderType;
        this.paymentMethod = paymentMethod;
        this.kasir = kasir;
        
        // Calculate totals
        this.subtotal = items.stream().mapToDouble(CartItem::getSubtotal).sum();
        this.discount = subtotal > 50000 ? subtotal * 0.1 : 0;
        this.total = subtotal - discount;
    }
}