package com.zayyani.cafe.model;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class Cart {
    private List<CartItem> items = new ArrayList<>();

    public void addItem(CartItem item) {
        // Check if item already exists with same customization
        for (CartItem cartItem : items) {
            if (cartItem.getMenuItem().getNama().equals(item.getMenuItem().getNama()) &&
                cartItem.getCustomization().equals(item.getCustomization())) {
                // Update quantity instead of adding new item
                cartItem.setQuantity(cartItem.getQuantity() + item.getQuantity());
                return;
            }
        }
        items.add(item);
    }

    public void removeItem(int index) {
        if (index >= 0 && index < items.size()) {
            items.remove(index);
        }
    }

    public void updateQuantity(int index, int quantity) {
        if (index >= 0 && index < items.size() && quantity > 0) {
            items.get(index).setQuantity(quantity);
        }
    }

    public void clear() {
        items.clear();
    }

    public double getTotal() {
        return items.stream().mapToDouble(CartItem::getSubtotal).sum();
    }

    public double getDiscountedTotal() {
        double total = getTotal();
        // Apply 10% discount for orders over 50,000
        if (total > 50000) {
            return total * 0.9;
        }
        return total;
    }

    public int getItemCount() {
        return items.stream().mapToInt(CartItem::getQuantity).sum();
    }
}