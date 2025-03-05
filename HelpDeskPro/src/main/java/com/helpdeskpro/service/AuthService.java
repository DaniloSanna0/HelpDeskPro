package com.helpdeskpro.service;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.helpdeskpro.model.Role;
import com.helpdeskpro.model.User;
import com.helpdeskpro.repository.UserRepository;
import com.helpdeskpro.security.JwtUtil;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = new BCryptPasswordEncoder(); 
    }

    public String register(String username, String password, Role role) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username già in uso");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);  // ✅ CORRETTO

        userRepository.save(user);

        return jwtUtil.generateToken(username, user.getRole().name());
    }

    public String login(String username, String password) {
        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isEmpty()) {
            throw new RuntimeException("Utente non trovato");
        }

        User user = optionalUser.get();

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Password errata");
        }

        return jwtUtil.generateToken(username, user.getRole().name());
    }
}
