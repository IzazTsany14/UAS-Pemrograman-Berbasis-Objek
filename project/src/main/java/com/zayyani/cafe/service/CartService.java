
package com.zayyani.cafe.service;

import com.zayyani.cafe.model.*;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;
import java.util.List;
import java.util.stream.Collectors;

@Service
@SessionScope
public class CartService {
    private final Cart cart = new Cart();
    
    public Cart getCart() {
        return cart;
    }
    
    public void addToCart(MenuItem menuItem, int quantity, String customization) {
        CartItem newItem = new CartItem(menuItem, quantity, customization);
        cart.addItem(newItem);
    }
    
    public void removeFromCart(int index) {
        cart.removeItem(index);
    }
    
    public void removeSelectedItems(List<String> itemNames) {
        List<CartItem> remainingItems = cart.getItems().stream()
            .filter(item -> !itemNames.contains(item.getMenuItem().getNama()))
            .collect(Collectors.toList());
        
        cart.getItems().clear();
        remainingItems.forEach(cart::addItem);
    }
    
    public void updateQuantity(int index, int quantity) {
        cart.updateQuantity(index, quantity);
    }
    
    public void clearCart() {
        cart.clear();
    }
    
    public double getTotal() {
        return cart.getTotal();
    }
    
    public double getDiscountedTotal() {
        return cart.getDiscountedTotal();
    }
    
    public int getItemCount() {
        return cart.getItemCount();
    }
}