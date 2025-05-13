package com.zayyani.cafe.controller;

import com.zayyani.cafe.service.CartService;
import com.zayyani.cafe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/cart")
public class CartController {
    
    @Autowired
    private CartService cartService;
    
    @Autowired
    private UserService userService;
    
    @GetMapping
    public String viewCart(Model model, HttpSession session) {
        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }
        
        model.addAttribute("cart", cartService.getCart());
        model.addAttribute("total", cartService.getTotal());
        model.addAttribute("discountedTotal", cartService.getDiscountedTotal());
        model.addAttribute("discount", cartService.getTotal() - cartService.getDiscountedTotal());
        model.addAttribute("hasDiscount", cartService.getTotal() > 50000);
        
        String username = (String) session.getAttribute("username");
        model.addAttribute("username", username);
        model.addAttribute("role", userService.getUserRole(username));
        model.addAttribute("cartCount", cartService.getItemCount());
        
        return "cart";
    }
    
    @PostMapping("/update")
    public String updateCart(@RequestParam int index, @RequestParam int quantity) {
        cartService.updateQuantity(index, quantity);
        return "redirect:/cart";
    }
    
    @GetMapping("/remove/{index}")
    public String removeItem(@PathVariable int index) {
        cartService.removeFromCart(index);
        return "redirect:/cart";
    }
    
    @GetMapping("/clear")
    public String clearCart() {
        cartService.clearCart();
        return "redirect:/cart";
    }
}