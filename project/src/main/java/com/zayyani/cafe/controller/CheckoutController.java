
package com.zayyani.cafe.controller;

import com.zayyani.cafe.model.Cart;
import com.zayyani.cafe.model.Order;
import com.zayyani.cafe.service.CartService;
import com.zayyani.cafe.service.OrderService;
import com.zayyani.cafe.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {
    
    @Autowired
    private CartService cartService;
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private UserService userService;
    
    @GetMapping
    public String checkout(Model model, HttpSession session) {
        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }
        
        Cart cart = cartService.getCart();
        
        if (cart.getItemCount() == 0) {
            return "redirect:/menu";
        }
        
        model.addAttribute("cart", cart);
        model.addAttribute("total", cartService.getTotal());
        model.addAttribute("discountedTotal", cartService.getDiscountedTotal());
        model.addAttribute("discount", cartService.getTotal() - cartService.getDiscountedTotal());
        model.addAttribute("hasDiscount", cartService.getTotal() > 50000);
        
        String username = (String) session.getAttribute("username");
        model.addAttribute("username", username);
        model.addAttribute("role", userService.getUserRole(username));
        model.addAttribute("cartCount", cartService.getItemCount());
        
        return "checkout";
    }
    
    @PostMapping("/place-order")
    public String placeOrder(@RequestParam String orderType, 
                            @RequestParam String paymentMethod,
                            @RequestParam(required = false) Double amountPaid,
                            @RequestParam(required = false) List<String> selectedItems,
                            HttpSession session) {
        
        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }
        
        Cart cart = cartService.getCart();
        String username = (String) session.getAttribute("username");
        
        // Create and save the order for selected items only
        Order order = orderService.createOrder(cart, orderType, paymentMethod, username);
        
        // Process payment if applicable
        if (amountPaid != null) {
            orderService.processPayment(order, amountPaid);
        }
        
        // Store order ID in session for receipt
        session.setAttribute("lastOrderId", order.getId());
        
        // Remove only the selected items from cart
        if (selectedItems != null && !selectedItems.isEmpty()) {
            cartService.removeSelectedItems(selectedItems);
        } else {
            // If no specific items selected, clear entire cart
            cartService.clearCart();
        }
        
        return "redirect:/menu";
    }
}