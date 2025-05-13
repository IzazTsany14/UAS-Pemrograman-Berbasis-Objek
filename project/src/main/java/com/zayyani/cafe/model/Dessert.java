package com.zayyani.cafe.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Dessert extends Menu {
    private String tipe;
    private KategoriDessert kategori;
    private String sajian; // Frozen/Cold/Hot
    private double extraPrice; // Additional price for customizations

    public Dessert(String nama, String tipe, double harga, int jumlah, KategoriDessert kategori) {
        super(nama, harga, jumlah, 0.08); // Diskon 8%
        this.tipe = tipe;
        this.kategori = kategori;
        this.sajian = kategori.toString(); // Default based on category
        this.extraPrice = 0.0;
        
        // Set image based on dessert type
        if (nama.toLowerCase().contains("ice") || kategori == KategoriDessert.FROZEN) {
            this.imageUrl = "/images/ice-cream.jpg";
        } else if (kategori == KategoriDessert.HOT) {
            this.imageUrl = "/images/hot-dessert.jpg";
        } else {
            this.imageUrl = "/images/dessert.jpg";
        }
    }

    @Override
    public double getTotalPrice() {
        return super.getTotalPrice() + (extraPrice * jumlah);
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("Tipe: " + tipe);
        System.out.println("Kategori: " + kategori);
        System.out.println("Sajian: " + sajian);
    }
}