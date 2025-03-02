package com.helpdeskpro.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.helpdeskpro.model.Role;
import com.helpdeskpro.service.AuthService;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody Map<String, String> request) {
        try {
            String username = request.get("username");
            String password = request.get("password");
            String roleString = request.get("role").toUpperCase(); // Converte in maiuscolo per sicurezza

            Role role = Role.valueOf(roleString); // Converte la stringa in un enum

            String token = authService.register(username, password, role);

            return ResponseEntity.ok(Map.of("token", token));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Ruolo non valido: " + request.get("role")));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> request) {
        try {
            String token = authService.login(request.get("username"), request.get("password"));
            return ResponseEntity.ok(Map.of("token", token));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
