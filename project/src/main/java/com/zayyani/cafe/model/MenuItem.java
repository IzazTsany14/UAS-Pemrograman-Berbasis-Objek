package com.zayyani.cafe.model;

public interface MenuItem {
    void displayInfo();
    double getHarga();
    String getNama();
    double getTotalPrice();
    void setHarga(double harga);
}