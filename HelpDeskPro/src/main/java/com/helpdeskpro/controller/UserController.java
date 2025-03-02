package com.helpdeskpro.controller;

import com.helpdeskpro.model.User;
import com.helpdeskpro.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Solo gli Admin possono vedere tutti gli utenti
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    // Ottieni il proprio profilo
    @GetMapping("/me")
    public ResponseEntity<User> getMyProfile(@RequestParam String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Utente non trovato"));
        return ResponseEntity.ok(user);
    }
}
