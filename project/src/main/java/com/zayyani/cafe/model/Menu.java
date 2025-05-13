package com.zayyani.cafe.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Menu implements MenuItem {
    protected String nama;
    protected double harga;
    protected int jumlah;
    protected double diskonRate;
    protected String imageUrl;

    public Menu(String nama, double harga, int jumlah, double diskonRate) {
        this.nama = nama;
        this.harga = harga;
        this.jumlah = jumlah;
        this.diskonRate = diskonRate;
        this.imageUrl = "/images/default.jpg"; // Default image
    }

    @Override
    public String getNama() {
        return nama;
    }

    @Override
    public double getHarga() {
        return harga;
    }

    @Override
    public void setHarga(double harga) {
        this.harga = harga;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public double calculatePriceAfterDiscount() {
        double totalHarga = harga * jumlah;
        double diskon = totalHarga * diskonRate;
        return totalHarga - diskon;
    }

    @Override
    public double getTotalPrice() {
        return calculatePriceAfterDiscount();
    }

    @Override
    public void displayInfo() {
        System.out.println("Nama Menu: " + nama);
        System.out.println("Harga per item: " + harga);
        System.out.println("Jumlah Dipesan: " + jumlah);
        System.out.println("Diskon Rate: " + (diskonRate * 100) + "%");
        System.out.println("Harga Setelah Diskon: " + calculatePriceAfterDiscount());
    }

    // Inner classes for categories
    public static class KategoriMinuman {
        public static final KategoriMinuman DINGIN = new KategoriMinuman("Dingin");
        public static final KategoriMinuman HANGAT = new KategoriMinuman("Hangat");

        private String name;

        private KategoriMinuman(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public static class KategoriMakanan {
        public static final KategoriMakanan ORIGINAL = new KategoriMakanan("Original");
        public static final KategoriMakanan SPICY = new KategoriMakanan("Spicy");

        private String name;

        private KategoriMakanan(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public static class KategoriDessert {
        public static final KategoriDessert FROZEN = new KategoriDessert("Frozen");
        public static final KategoriDessert COLD = new KategoriDessert("Cold");
        public static final KategoriDessert HOT = new KategoriDessert("Hot");

        private String name;

        private KategoriDessert(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}