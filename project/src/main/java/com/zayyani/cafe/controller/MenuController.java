package com.zayyani.cafe.controller;

import com.zayyani.cafe.model.*;
import com.zayyani.cafe.service.CartService;
import com.zayyani.cafe.service.MenuService;
import com.zayyani.cafe.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping("/menu")
public class MenuController {
    
    @Autowired
    private MenuService menuService;
    
    @Autowired
    private CartService cartService;
    
    @Autowired
    private UserService userService;
    
    @GetMapping
    public String getMenu(@RequestParam(defaultValue = "all") String category, Model model, HttpSession session) {
        Map<String, Object> menuItems = menuService.getMenuByCategory(category);
        
        model.addAttribute("minumanList", menuItems.get("minuman"));
        model.addAttribute("makananList", menuItems.get("makanan"));
        model.addAttribute("dessertList", menuItems.get("dessert"));
        model.addAttribute("selectedCategory", category);
        
        String username = (String) session.getAttribute("username");
        if (username != null) {
            model.addAttribute("username", username);
            model.addAttribute("role", userService.getUserRole(username));
            model.addAttribute("cartCount", cartService.getItemCount());
        }
        
        return "menu";
    }
    
    @GetMapping("/detail/{type}/{name}")
    public String getMenuDetail(@PathVariable String type, @PathVariable String name, 
                               Model model, HttpSession session) {
        MenuItem menuItem = menuService.getItemByNameAndType(name, type);
        
        if (menuItem == null) {
            return "redirect:/menu";
        }
        
        model.addAttribute("item", menuItem);
        model.addAttribute("type", type);
        
        // Add customization options based on item type
        if (type.equalsIgnoreCase("makanan")) {
            model.addAttribute("levelPedasOptions", new String[]{"Original", "Mild", "Spicy", "Extra Spicy"});
        } else if (type.equalsIgnoreCase("minuman")) {
            String[] suhuOptions;
            Minuman minuman = (Minuman) menuItem;
            if (minuman.getKategori() == Menu.KategoriMinuman.DINGIN) {
                suhuOptions = new String[]{"Cold", "Extra Ice"};
            } else {
                suhuOptions = new String[]{"Hot"};
            }
            model.addAttribute("suhuOptions", suhuOptions);
        } else if (type.equalsIgnoreCase("dessert")) {
            model.addAttribute("sajianOptions", new String[]{"Frozen", "Cold", "Hot"});
        }
        
        String username = (String) session.getAttribute("username");
        if (username != null) {
            model.addAttribute("username", username);
            model.addAttribute("role", userService.getUserRole(username));
            model.addAttribute("cartCount", cartService.getItemCount());
        }
        
        return "menu-detail";
    }
    
    @PostMapping("/add-to-cart")
    public String addToCart(@RequestParam String name, @RequestParam String type,
                            @RequestParam(defaultValue = "1") int quantity,
                            @RequestParam(required = false) String levelPedas,
                            @RequestParam(required = false) String suhuOption,
                            @RequestParam(required = false) String sajian,
                            HttpSession session) {
        
        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }
        
        MenuItem menuItem = menuService.getItemByNameAndType(name, type);
        
        if (menuItem != null) {
            double extraPrice = 0.0;
            String customization = "";
            
            // Apply customizations based on item type
            if (type.equalsIgnoreCase("makanan") && levelPedas != null) {
                Makanan makanan = (Makanan) menuItem;
                extraPrice = menuService.calculateExtraPriceForMakanan(levelPedas);
                makanan.setLevelPedas(levelPedas);
                makanan.setExtraPrice(extraPrice);
                customization = "Level Pedas: " + levelPedas;
                
            } else if (type.equalsIgnoreCase("minuman") && suhuOption != null) {
                Minuman minuman = (Minuman) menuItem;
                extraPrice = menuService.calculateExtraPriceForMinuman(suhuOption);
                minuman.setSuhuOption(suhuOption);
                minuman.setExtraPrice(extraPrice);
                customization = "Suhu: " + suhuOption;
                
            } else if (type.equalsIgnoreCase("dessert") && sajian != null) {
                Dessert dessert = (Dessert) menuItem;
                dessert.setSajian(sajian);
                customization = "Sajian: " + sajian;
            }
            
            cartService.addToCart(menuItem, quantity, customization);
        }
        
        return "redirect:/cart";
    }
}