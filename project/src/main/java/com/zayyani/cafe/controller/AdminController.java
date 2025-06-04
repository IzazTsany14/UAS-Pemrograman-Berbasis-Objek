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
import org.springframework.web.multipart.MultipartFile;

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
            .mapToDouble(Order::getTotal)
            .sum();
            
        // Get sales by category
        Map<String, Long> salesByCategory = orders.stream()
            .flatMap(order -> order.getItems().stream())
            .filter(item -> item != null && item.getMenuItem() != null)
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

    // Perbaikan: Mapping receipt di luar method adminDashboard
    @GetMapping("/receipt/{orderId}")
    public String viewReceipt(@PathVariable String orderId, Model model) {
        Order order = orderService.getOrderById(orderId);
        if (order == null) {
            return "redirect:/admin";
        }
        model.addAttribute("order", order);
        return "receipt";
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
        String role = (String) session.getAttribute("role");
        
        if (role == null || !role.equals("admin")) {
            return "redirect:/login";
        }
        
        menuService.updatePrice(name, price);
        
        return "redirect:/admin/menu";
    }

    @PostMapping("/update-menu")
    public String updateMenu(
        @RequestParam String oldName,
        @RequestParam String type,
        @RequestParam String newName,
        @RequestParam String newCategory,
        HttpSession session
    ) {
        String role = (String) session.getAttribute("role");
        if (role == null || !role.equals("admin")) {
            return "redirect:/login";
        }
        menuService.updateMenu(oldName, type, newName, newCategory);
        return "redirect:/admin/menu";
    }
    
    @GetMapping("/add-menu")
    public String showAddMenuForm(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        String role = (String) session.getAttribute("role");
        
        if (username == null || !role.equals("admin")) {
            return "redirect:/login";
        }
        
        model.addAttribute("username", username);
        
        return "admin/add-menu";
    }
        
    @PostMapping("/add-menu")
    public String addMenuItem(
        @RequestParam String type,
        @RequestParam String name,
        @RequestParam double price,
        @RequestParam(required = false) String kategori,
        @RequestParam(required = false) String minumanType,
        @RequestParam(required = false) String ukuranGelas,
        @RequestParam(required = false) String tipe,
        @RequestParam(required = false) MultipartFile imageFile, // tambahkan ini
        HttpSession session
    ) {
        String role = (String) session.getAttribute("role");
        if (role == null || !role.equals("admin")) {
            return "redirect:/login";
        }

    String imageUrl = null;
    if (imageFile != null && !imageFile.isEmpty()) {
        try {
            String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
            String uploadDir = "src/main/resources/static/gambar/";
            java.nio.file.Path path = java.nio.file.Paths.get(uploadDir + fileName);
            java.nio.file.Files.copy(imageFile.getInputStream(), path, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            imageUrl = "/gambar/" + fileName;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    switch (type.toLowerCase()) {
        case "makanan":
            Menu.KategoriMakanan katMakanan = "Spicy".equalsIgnoreCase(kategori) ?
                Menu.KategoriMakanan.SPICY : Menu.KategoriMakanan.ORIGINAL;
            menuService.addMakanan(name, price, katMakanan, imageUrl);
            break;
        case "minuman":
            Menu.KategoriMinuman katMinuman = "Hangat".equalsIgnoreCase(kategori) ?
                Menu.KategoriMinuman.HANGAT : Menu.KategoriMinuman.DINGIN;
            menuService.addMinuman(name, minumanType, ukuranGelas, price, katMinuman, imageUrl);
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
            menuService.addDessert(name, tipe, price, katDessert, imageUrl);
            break;
        }

    return "redirect:/admin/menu";
    }
    
    @GetMapping("/orders")
    public String orderManagement(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        String role = (String) session.getAttribute("role");
        
        if (username == null || !role.equals("admin")) {
            return "redirect:/login";
        }
        
        model.addAttribute("username", username);
        model.addAttribute("orders", orderService.getAllOrders());
        
        return "admin/orders";
    }
    @PostMapping("/delete-menu")
    @ResponseBody
    public String deleteMenu(@RequestParam String name, @RequestParam String type, HttpSession session) {
        String role = (String) session.getAttribute("role");
        if (role == null || !role.equals("admin")) {
            return "unauthorized";
        }
        boolean success = menuService.deleteMenuByNameAndType(name, type);
        return success ? "ok" : "fail";
    }
    @GetMapping("/gambar/{filename:.+}")
    @ResponseBody
    public org.springframework.core.io.Resource serveImage(@PathVariable String filename, @RequestParam(required = false) String imageUrl) {
        try {
            // Jika imageUrl diberikan, gunakan imageUrl, jika tidak gunakan filename
            String filePath;
            if (imageUrl != null && !imageUrl.isEmpty()) {
                // Hilangkan prefix "/gambar/" jika ada
                String cleanImageUrl = imageUrl.replaceFirst("^/gambar/", "");
                filePath = "src/main/resources/static/gambar/" + cleanImageUrl;
            } else {
                filePath = "src/main/resources/static/gambar/" + filename;
            }
            java.nio.file.Path file = java.nio.file.Paths.get(filePath).normalize();
            org.springframework.core.io.Resource resource = new org.springframework.core.io.UrlResource(file.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read file: " + filename);
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not serve file: " + filename, e);
        }
    }
}
