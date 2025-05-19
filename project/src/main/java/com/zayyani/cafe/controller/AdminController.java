package com.zayyani.cafe.controller;

import com.zayyani.cafe.model.Dessert;
import com.zayyani.cafe.model.Makanan;
import com.zayyani.cafe.model.Menu;
import com.zayyani.cafe.model.MenuItem;
import com.zayyani.cafe.model.Minuman;
import com.zayyani.cafe.service.MenuService;
import com.zayyani.cafe.service.OrderService;
import com.zayyani.cafe.service.UserService;
import com.zayyani.cafe.model.Order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {
    
    @Autowired
    private MenuService menuService;
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private UserService userService;
    
    @GetMapping
    public String adminDashboard(HttpSession session, Model model) {
        if (!validateAdminAccess(session)) {
            return "redirect:/login";
        }
        
        String username = (String) session.getAttribute("username");
        List<Order> orders = orderService.getAllOrders();
        
        // Calculate total revenue
        double totalRevenue = orders.stream()
        .mapToDouble(Order::getTotal) // Error disini
        .sum();
    
            
        // Get sales by category
        // Get sales by category
Map<String, Long> salesByCategory = orders.stream()
    .flatMap(order -> order.getItems().stream())
    .filter(item -> item != null && item.getMenuItem() != null) // Add null checks
    .collect(Collectors.groupingBy(
        item -> getItemCategory(item.getMenuItem()),
        Collectors.counting()
    ));
        
        model.addAttribute("username", username);
        model.addAttribute("minumanList", menuService.getAllMinuman());
        model.addAttribute("makananList", menuService.getAllMakanan());
        model.addAttribute("dessertList", menuService.getAllDessert());
        model.addAttribute("orders", orders);
        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("salesByCategory", salesByCategory);
        
        return "admin/dashboard";
    }
    
    private String getItemCategory(MenuItem item) {
        if (item instanceof Minuman) return "Minuman";
        if (item instanceof Makanan) return "Makanan";
        if (item instanceof Dessert) return "Dessert";
        return "Other";
    }
    
    private boolean validateAdminAccess(HttpSession session) {
        String username = (String) session.getAttribute("username");
        String role = (String) session.getAttribute("role");
        return username != null && role != null && role.equals("admin");
    }
    
    @GetMapping("/menu")
    public String menuManagement(HttpSession session, Model model) {
        // Check if user is logged in and has admin role
        String username = (String) session.getAttribute("username");
        String role = (String) session.getAttribute("role");
        
        if (username == null || !role.equals("admin")) {
            return "redirect:/login";
        }
        
        model.addAttribute("username", username);
        model.addAttribute("minumanList", menuService.getAllMinuman());
        model.addAttribute("makananList", menuService.getAllMakanan());
        model.addAttribute("dessertList", menuService.getAllDessert());
        
        return "admin/menu-management";
    }
    
    @PostMapping("/update-price")
    public String updatePrice(@RequestParam String name, @RequestParam double price, 
                             HttpSession session) {
        // Check if user is logged in and has admin role
        String role = (String) session.getAttribute("role");
        
        if (role == null || !role.equals("admin")) {
            return "redirect:/login";
        }
        
        menuService.updatePrice(name, price);
        
        return "redirect:/admin/menu";
    }
    
    @GetMapping("/add-menu")
    public String showAddMenuForm(HttpSession session, Model model) {
        // Check if user is logged in and has admin role
        String username = (String) session.getAttribute("username");
        String role = (String) session.getAttribute("role");
        
        if (username == null || !role.equals("admin")) {
            return "redirect:/login";
        }
        
        model.addAttribute("username", username);
        
        return "admin/add-menu";
    }
    
    @PostMapping("/add-menu")
    public String addMenuItem(@RequestParam String type, 
                             @RequestParam String name,
                             @RequestParam double price,
                             @RequestParam(required = false) String kategori,
                             @RequestParam(required = false) String minumanType,
                             @RequestParam(required = false) String ukuranGelas,
                             @RequestParam(required = false) String tipe,
                             HttpSession session) {
        
        // Check if user is logged in and has admin role
        String role = (String) session.getAttribute("role");
        
        if (role == null || !role.equals("admin")) {
            return "redirect:/login";
        }
        
        switch (type.toLowerCase()) {
            case "makanan":
                Menu.KategoriMakanan katMakanan = "Spicy".equalsIgnoreCase(kategori) ? 
                    Menu.KategoriMakanan.SPICY : Menu.KategoriMakanan.ORIGINAL;
                menuService.addMakanan(name, price, katMakanan);
                break;
                
            case "minuman":
                Menu.KategoriMinuman katMinuman = "Hangat".equalsIgnoreCase(kategori) ? 
                    Menu.KategoriMinuman.HANGAT : Menu.KategoriMinuman.DINGIN;
                menuService.addMinuman(name, minumanType, ukuranGelas, price, katMinuman);
                break;
                
            case "dessert":
                Menu.KategoriDessert katDessert;
                if ("Frozen".equalsIgnoreCase(kategori)) {
                    katDessert = Menu.KategoriDessert.FROZEN;
                } else if ("Hot".equalsIgnoreCase(kategori)) {
                    katDessert = Menu.KategoriDessert.HOT;
                } else {
                    katDessert = Menu.KategoriDessert.COLD;
                }
                menuService.addDessert(name, tipe, price, katDessert);
                break;
        }
        
        return "redirect:/admin/menu";
    }
    
    @GetMapping("/orders")
    public String orderManagement(HttpSession session, Model model) {
        // Check if user is logged in and has admin role
        String username = (String) session.getAttribute("username");
        String role = (String) session.getAttribute("role");
        
        if (username == null || !role.equals("admin")) {
            return "redirect:/login";
        }
        
        model.addAttribute("username", username);
        model.addAttribute("orders", orderService.getAllOrders());
        
        return "admin/order-management";
    }
}