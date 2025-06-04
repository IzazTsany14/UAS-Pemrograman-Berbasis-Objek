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

    public Minuman(String namaMinuman, String typeMinuman, String ukuranGelas, double harga, int jumlahDipesan, KategoriMinuman kategori, String imgUrl) {
    super(namaMinuman, harga, jumlahDipesan, 0.15, imgUrl);
    this.typeMinuman = typeMinuman;
    this.ukuranGelas = ukuranGelas;
    this.kategori = kategori;
    this.suhuOption = kategori == KategoriMinuman.DINGIN ? "Cold" : "Hot";
    this.extraPrice = 0.0;
    
    if (imgUrl != null && !imgUrl.isEmpty()) {
        this.imageUrl = imgUrl;
    } else {
        // Atur gambar default berdasarkan jenis minuman
        switch (namaMinuman.toLowerCase()) {
            case "brown sugar":
                this.imageUrl = "/images/brownsugar.jpg";
                break;
            case "caramel macchiato":
                this.imageUrl = "/images/caramelmacchiato.jpg";
                break;
            case "americano coffee":
                this.imageUrl = "/images/americano.jpg";
                break;
            case "matcha latte":
                this.imageUrl = "/images/matchalatte.jpg";
                break;
            case "taro":
                this.imageUrl = "/images/taro.jpg";
                break;
            default:
                this.imageUrl = typeMinuman.equalsIgnoreCase("Coffee") ? 
                    "/images/coffee.jpg" : "/images/non-coffee.jpg";
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
        System.out.println("Type Minuman: " + typeMinuman);
        System.out.println("Ukuran Gelas: " + ukuranGelas);
        System.out.println("Kategori: " + kategori);
        System.out.println("Suhu Option: " + suhuOption);
    }
}
