package com.zayyani.cafe.service;

import com.zayyani.cafe.model.User;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final List<User> users = new ArrayList<>();
    
    @PostConstruct
    public void init() {
        // Initialize admin user
        users.add(new User("admin", "zayyaniroar", "admin"));
        // Default user
        users.add(new User("user", "user", "user"));
    }

    public void addUser(User user) {
        users.add(user);
    }
    
    public Optional<User> findByUsername(String username) {
        return users.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }
    
    public boolean authenticate(String username, String password) {
        return users.stream()
                .anyMatch(user -> user.getUsername().equals(username) && user.getPassword().equals(password));
    }
    
    public String getUserRole(String username) {
        return users.stream()
                .filter(user -> user.getUsername().equals(username))
                .map(User::getRole)
                .findFirst()
                .orElse("user");
    }
}
