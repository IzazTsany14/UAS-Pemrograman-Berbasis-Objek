package com.zayyani.cafe.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Makanan extends Menu {
    private KategoriMakanan kategori;
    private String levelPedas; // Original/Mild/Spicy/Extra Spicy
    private double extraPrice; // Additional price for customizations

    public Makanan(String nama, double harga, int jumlah, KategoriMakanan kategori) {
        super(nama, harga, jumlah, 0.1); // Diskon 10%
        this.kategori = kategori;
        this.levelPedas = kategori == KategoriMakanan.SPICY ? "Spicy" : "Original"; // Default based on category
        this.extraPrice = 0.0;
        
        // Set image based on food name
        if (nama.toLowerCase().contains("ayam")) {
            this.imageUrl = "/images/chicken.jpg";
        } else if (nama.toLowerCase().contains("nasi")) {
            this.imageUrl = "/images/rice.jpg";
        } else if (nama.toLowerCase().contains("mie")) {
            this.imageUrl = "/images/noodle.jpg";
        } else {
            this.imageUrl = "/images/food.jpg";
        }
    }

    @Override
    public double getTotalPrice() {
        return super.getTotalPrice() + (extraPrice * jumlah);
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("Kategori: " + kategori);
        System.out.println("Level Pedas: " + levelPedas);
    }
}