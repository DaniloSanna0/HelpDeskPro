package com.helpdeskpro.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.helpdeskpro.model.User;
import com.helpdeskpro.repository.UserRepository;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public JwtFilter(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("❌ Nessun token trovato, richiesta anonima.");
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        System.out.println("🔹 Token ricevuto: " + token);

        String username = jwtUtil.extractUsername(token);
        System.out.println("🔹 Username estratto dal token: " + username);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("❌ Utente non trovato nel database"));

            if (jwtUtil.validateToken(token, username)) {
                System.out.println("✅ Token valido per: " + username);

                List<SimpleGrantedAuthority> authorities = Collections.singletonList(
                    new SimpleGrantedAuthority("ROLE_" + user.getRole().getName())
                );

                // Forza l'autenticazione nel SecurityContextHolder
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(user.getUsername(), null, authorities);

                SecurityContextHolder.getContext().setAuthentication(authentication);
                System.out.println("🔹 Autenticazione impostata nel SecurityContextHolder per " + username);
            } else {
                System.out.println("❌ Token non valido");
            }
        } else {
            System.out.println("❌ Username nullo o autenticazione già presente");
        }

        System.out.println("PRINCIPAL dopo autenticazione: " + SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        filterChain.doFilter(request, response);
    }


}
