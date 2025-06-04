package com.zayyani.cafe.controller;

import com.zayyani.cafe.model.User;
import com.zayyani.cafe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           Model model) {
        if (userService.findByUsername(username).isPresent()) {
            model.addAttribute("error", "Username sudah digunakan.");
            return "register";
        }
        userService.addUser(new User(username, password, "user"));
        model.addAttribute("success", "Registrasi berhasil! Silakan login.");
        return "register";
    }
}
