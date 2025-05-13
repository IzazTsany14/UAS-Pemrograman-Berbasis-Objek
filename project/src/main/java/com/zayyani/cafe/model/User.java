package com.zayyani.cafe.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private String username;
    private String password;
    private String role; // admin or user

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
}