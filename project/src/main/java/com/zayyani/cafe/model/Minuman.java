package com.zayyani.cafe.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Minuman extends Menu {
    private String typeMinuman;
    private String ukuranGelas;
    private KategoriMinuman kategori;
    private String suhuOption; // Hot/Cold/Extra Ice
    private double extraPrice; // Additional price for customizations

    public Minuman(String namaMinuman, String typeMinuman, String ukuranGelas, double harga, int jumlahDipesan, KategoriMinuman kategori) {
        super(namaMinuman, harga, jumlahDipesan, 0.15); // Diskon 15%
        this.typeMinuman = typeMinuman;
        this.ukuranGelas = ukuranGelas;
        this.kategori = kategori;
        this.suhuOption = kategori == KategoriMinuman.DINGIN ? "Cold" : "Hot"; // Default based on category
        this.extraPrice = 0.0;
        
        // Assign different image URLs based on type
        if (typeMinuman.toLowerCase().contains("coffee")) {
            this.imageUrl = "/images/coffee.jpg";
        } else {
            this.imageUrl = "/images/non-coffee.jpg";
        }
    }

    @Override
    public double getTotalPrice() {
        return super.getTotalPrice() + (extraPrice * jumlah);
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("Type Minuman: " + typeMinuman);
        System.out.println("Ukuran Gelas: " + ukuranGelas);
        System.out.println("Kategori: " + kategori);
        System.out.println("Suhu Option: " + suhuOption);
    }
}