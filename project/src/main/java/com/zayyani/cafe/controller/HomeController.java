package com.zayyani.cafe.controller;

import com.zayyani.cafe.service.CartService;
import com.zayyani.cafe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private CartService cartService;
    
    @GetMapping("/")
    public String home(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username != null) {
            model.addAttribute("username", username);
            model.addAttribute("role", userService.getUserRole(username));
            model.addAttribute("cartCount", cartService.getItemCount());
        }
        return "index";
    }
    
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
    
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, 
                        HttpSession session, Model model) {
        if (userService.authenticate(username, password)) {
            session.setAttribute("username", username);
            session.setAttribute("role", userService.getUserRole(username));
            return "redirect:/";
        } else {
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
    
    @GetMapping("/about")
    public String about(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username != null) {
            model.addAttribute("username", username);
            model.addAttribute("role", userService.getUserRole(username));
            model.addAttribute("cartCount", cartService.getItemCount());
        }
        return "about";
    }
}