package com.zayyani.cafe.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItem {
    private MenuItem menuItem;
    private int quantity;
    private String customization; // Store customization details

    public CartItem(MenuItem menuItem, int quantity, String customization) {
        this.menuItem = menuItem;
        this.quantity = quantity;
        this.customization = customization;
    }

    public double getSubtotal() {
        return menuItem.getTotalPrice() * quantity;
    }
}
