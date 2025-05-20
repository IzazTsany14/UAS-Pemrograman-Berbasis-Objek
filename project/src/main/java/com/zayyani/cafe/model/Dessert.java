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

    public Dessert(String nama, String tipe, double harga, int jumlah, KategoriDessert kategori, String imgUrl) {
    super(nama, harga, jumlah, 0.08);
    this.tipe = tipe;
    this.kategori = kategori;
    this.sajian = kategori.toString();
    this.extraPrice = 0.0;
    
    if (imgUrl != null && !imgUrl.isEmpty()) {
        this.imageUrl = imgUrl;
    } else {
        // Atur gambar default berdasarkan nama dessert
        switch (nama.toLowerCase()) {
            case "cheesecake":
                this.imageUrl = "/images/cheesecake.jpg";
                break;
            case "chocolate lava":
                this.imageUrl = "/images/chocolate-lava.jpg";
                break;
            case "ice cream sundae":
                this.imageUrl = "/images/ice-cream-sundae.jpg";
                break;
            case "tiramisu":
                this.imageUrl = "/images/tiramisu.jpg";
                break;
            default:
                this.imageUrl = "/images/dessert.jpg";
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
        System.out.println("Tipe: " + tipe);
        System.out.println("Kategori: " + kategori);
        System.out.println("Sajian: " + sajian);
    }
}
