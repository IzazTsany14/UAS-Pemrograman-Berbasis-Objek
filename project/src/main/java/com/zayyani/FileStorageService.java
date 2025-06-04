package com.zayyani.cafe.service;

import com.zayyani.cafe.model.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import jakarta.annotation.PostConstruct;

@Service
public class MenuService {
    private final List<Minuman> minumanList = new ArrayList<>();
    private final List<Makanan> makananList = new ArrayList<>();
    private final List<Dessert> dessertList = new ArrayList<>();

    @PostConstruct
    public void init() {
    // Initialize menu items dengan imageUrl default
    // Minuman
    minumanList.add(new Minuman("Brown Sugar", "Non Coffee", "Large", 17000, 1, Menu.KategoriMinuman.DINGIN, ""));
    minumanList.add(new Minuman("Caramel Macchiato", "Cold Brew", "Medium", 20000, 1, Menu.KategoriMinuman.DINGIN, ""));
    minumanList.add(new Minuman("Americano Coffee", "Coffee", "Small", 15000, 1, Menu.KategoriMinuman.HANGAT, ""));
    minumanList.add(new Minuman("Matcha Latte", "Non Coffee", "Medium", 9000, 1, Menu.KategoriMinuman.DINGIN, ""));
    minumanList.add(new Minuman("Taro", "Non Coffee", "Small", 13000, 1, Menu.KategoriMinuman.DINGIN, ""));

    // Makanan
    makananList.add(new Makanan("Nasi Goreng", 15000, 1, Menu.KategoriMakanan.ORIGINAL, ""));
    makananList.add(new Makanan("Mie Ayam", 8000, 1, Menu.KategoriMakanan.ORIGINAL, ""));
    makananList.add(new Makanan("Ayam Geprek", 17000, 1, Menu.KategoriMakanan.SPICY, ""));
    makananList.add(new Makanan("Ayam Goreng", 18000, 1, Menu.KategoriMakanan.ORIGINAL, ""));

    // Dessert
    dessertList.add(new Dessert("Cheesecake", "Sweet", 10000, 1, Menu.KategoriDessert.COLD, ""));
    dessertList.add(new Dessert("Chocolate Lava", "Sweet", 20000, 1, Menu.KategoriDessert.HOT, ""));
    dessertList.add(new Dessert("Ice Cream Sundae", "Cold", 15000, 1, Menu.KategoriDessert.FROZEN, ""));
    dessertList.add(new Dessert("Tiramisu", "Sweet", 14000, 1, Menu.KategoriDessert.HOT, ""));
    }

    public List<Minuman> getAllMinuman() {
        return minumanList;
    }

    public List<Makanan> getAllMakanan() {
        return makananList;
    }

    public List<Dessert> getAllDessert() {
        return dessertList;
    }

    public Map<String, Object> getMenuByCategory(String category) {
        Map<String, Object> result = Map.of(
            "minuman", category.equals("all") || category.equals("minuman") ? minumanList : List.of(),
            "makanan", category.equals("all") || category.equals("makanan") ? makananList : List.of(),
            "dessert", category.equals("all") || category.equals("dessert") ? dessertList : List.of()
        );
        return result;
    }

    public Optional<MenuItem> findItemByName(String name) {
        Optional<Minuman> minuman = minumanList.stream()
                .filter(m -> m.getNama().equals(name))
                .findFirst();
        
        if (minuman.isPresent()) {
            return Optional.of(minuman.get());
        }
        
        Optional<Makanan> makanan = makananList.stream()
                .filter(m -> m.getNama().equals(name))
                .findFirst();
        
        if (makanan.isPresent()) {
            return Optional.of(makanan.get());
        }
        
        Optional<Dessert> dessert = dessertList.stream()
                .filter(d -> d.getNama().equals(name))
                .findFirst();
        
        if (dessert.isPresent()) {
            return Optional.of(dessert.get());
        }
        
        return Optional.empty();
    }

    public MenuItem getItemByNameAndType(String name, String type) {
        switch (type.toLowerCase()) {
            case "minuman":
                return minumanList.stream()
                        .filter(m -> m.getNama().equals(name))
                        .findFirst()
                        .orElse(null);
            case "makanan":
                return makananList.stream()
                        .filter(m -> m.getNama().equals(name))
                        .findFirst()
                        .orElse(null);
            case "dessert":
                return dessertList.stream()
                        .filter(d -> d.getNama().equals(name))
                        .findFirst()
                        .orElse(null);
            default:
                return null;
        }
    }

    public boolean updatePrice(String name, double newPrice) {
        boolean updated = false;
        
        for (Minuman minuman : minumanList) {
            if (minuman.getNama().equalsIgnoreCase(name)) {
                minuman.setHarga(newPrice);
                updated = true;
                break;
            }
        }
        
        if (!updated) {
            for (Makanan makanan : makananList) {
                if (makanan.getNama().equalsIgnoreCase(name)) {
                    makanan.setHarga(newPrice);
                    updated = true;
                    break;
                }
            }
        }
        
        if (!updated) {
            for (Dessert dessert : dessertList) {
                if (dessert.getNama().equalsIgnoreCase(name)) {
                    dessert.setHarga(newPrice);
                    updated = true;
                    break;
                }
            }
        }
        
        return updated;
    }
    public boolean updateMenu(String oldName, String type, String newName, String newCategory) {
    boolean updated = false;
    switch (type.toLowerCase()) {
        case "makanan":
            for (Makanan makanan : makananList) {
                if (makanan.getNama().equalsIgnoreCase(oldName)) {
                    makanan.setNama(newName);
                    makanan.setKategori(
                        "Spicy".equalsIgnoreCase(newCategory) ? Menu.KategoriMakanan.SPICY : Menu.KategoriMakanan.ORIGINAL
                    );
                    updated = true;
                    break;
                }
            }
            break;
        case "minuman":
            for (Minuman minuman : minumanList) {
                if (minuman.getNama().equalsIgnoreCase(oldName)) {
                    minuman.setNama(newName);
                    minuman.setKategori(
                        "Hangat".equalsIgnoreCase(newCategory) ? Menu.KategoriMinuman.HANGAT : Menu.KategoriMinuman.DINGIN
                    );
                    updated = true;
                    break;
                }
            }
            break;
        case "dessert":
            for (Dessert dessert : dessertList) {
                if (dessert.getNama().equalsIgnoreCase(oldName)) {
                    dessert.setNama(newName);
                    if ("Frozen".equalsIgnoreCase(newCategory)) {
                        dessert.setKategori(Menu.KategoriDessert.FROZEN);
                    } else if ("Hot".equalsIgnoreCase(newCategory)) {
                        dessert.setKategori(Menu.KategoriDessert.HOT);
                    } else {
                        dessert.setKategori(Menu.KategoriDessert.COLD);
                    }
                    updated = true;
                    break;
                }
            }
            break;
        }
        return updated;
    }

    public void addMakanan(String name, double price, Menu.KategoriMakanan kategori, String imageUrl) {
    makananList.add(new Makanan(name, price, 1, kategori, imageUrl));
    }
    public void addMinuman(String name, String type, String size, double price, Menu.KategoriMinuman kategori, String imageUrl) {
        minumanList.add(new Minuman(name, type, size, price, 1, kategori, imageUrl));
    }
    public void addDessert(String name, String type, double price, Menu.KategoriDessert kategori, String imageUrl) {
        dessertList.add(new Dessert(name, type, price, 1, kategori, imageUrl));
    }

    // Method to customize Minuman
    public void customizeMinuman(String name, String suhuOption, double extraPrice) {
        minumanList.stream()
            .filter(m -> m.getNama().equals(name))
            .findFirst()
            .ifPresent(minuman -> {
                minuman.setSuhuOption(suhuOption);
                minuman.setExtraPrice(extraPrice);
            });
    }

    // Method to customize Makanan
    public void customizeMakanan(String name, String levelPedas, double extraPrice) {
        makananList.stream()
            .filter(m -> m.getNama().equals(name))
            .findFirst()
            .ifPresent(makanan -> {
                makanan.setLevelPedas(levelPedas);
                makanan.setExtraPrice(extraPrice);
            });
    }

    // Method to customize Dessert
    public void customizeDessert(String name, String sajian, double extraPrice) {
        dessertList.stream()
            .filter(d -> d.getNama().equals(name))
            .findFirst()
            .ifPresent(dessert -> {
                dessert.setSajian(sajian);
                dessert.setExtraPrice(extraPrice);
            });
    }

    public double calculateExtraPriceForMinuman(String suhuOption) {
        switch (suhuOption) {
            case "Extra Ice":
                return 3000.0;
            default:
                return 0.0;
        }
    }

    public double calculateExtraPriceForMakanan(String levelPedas) {
        switch (levelPedas) {
            case "Spicy":
                return 3000.0;
            case "Extra Spicy":
                return 5000.0;
            default:
                return 0.0;
        }
    }

    public boolean deleteMenuByNameAndType(String name, String type) {
    switch (type.toLowerCase()) {
        case "makanan":
            return makananList.removeIf(m -> m.getNama().equalsIgnoreCase(name));
        case "minuman":
            return minumanList.removeIf(m -> m.getNama().equalsIgnoreCase(name));
        case "dessert":
            return dessertList.removeIf(m -> m.getNama().equalsIgnoreCase(name));
        default:
            return false;
    }
}
}
