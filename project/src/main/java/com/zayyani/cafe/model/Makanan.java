package com.zayyani.cafe.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Makanan extends Menu {
    private KategoriMakanan kategori;
    private String levelPedas; // Original/Mild/Spicy/Extra Spicy
    private double extraPrice; // Additional price for customizations

    public Makanan(String nama, double harga, int jumlah, KategoriMakanan kategori, String imgUrl) {
    super(nama, harga, jumlah, 0.1);
    this.kategori = kategori;
    this.levelPedas = kategori == KategoriMakanan.SPICY ? "Spicy" : "Original";
    this.extraPrice = 0.0;

    if (imgUrl != null && !imgUrl.isEmpty()) {
        this.imageUrl = imgUrl;
    } else {
        // Atur gambar default berdasarkan nama makanan
        switch (nama.toLowerCase()) {
            case "nasi goreng":
                this.imageUrl = "/images/nasi-goreng.jpg";
                break;
            case "mie ayam":
                this.imageUrl = "/images/mie-ayam.jpg";
                break;
            case "ayam geprek":
                this.imageUrl = "/images/ayam-geprek.jpg";
                break;
            case "ayam goreng":
                this.imageUrl = "/images/ayam-goreng.jpg";
                break;
            default:
                this.imageUrl = "/images/food.jpg";
        }
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
