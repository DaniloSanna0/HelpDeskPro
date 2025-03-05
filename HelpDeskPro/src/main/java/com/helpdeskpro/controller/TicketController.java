package com.helpdeskpro.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.helpdeskpro.model.Ticket;
import com.helpdeskpro.model.TicketStatus;
import com.helpdeskpro.model.User;
import com.helpdeskpro.repository.TicketRepository;
import com.helpdeskpro.repository.UserRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    public TicketController(TicketRepository ticketRepository, UserRepository userRepository) {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
    }

    private User getAuthenticatedUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;

        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));
    }

    @PostMapping
    public ResponseEntity<Ticket> createTicket(@Valid @RequestBody Ticket ticket) {
        User user = getAuthenticatedUser();
        ticket.setUser(user);
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setCreatedAt(LocalDateTime.now());  
        ticketRepository.save(ticket);
        return ResponseEntity.ok(ticket);
    }

    @GetMapping
    public ResponseEntity<List<Ticket>> getUserTickets() {
        User user = getAuthenticatedUser();
        List<Ticket> tickets = ticketRepository.findByUser(user);
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/all") 
    public ResponseEntity<List<Ticket>> getAllTickets() {
        User user = getAuthenticatedUser();

        if (user.getRole().name().equals("ADMIN")) { 
            throw new RuntimeException("Accesso negato");
        }

        List<Ticket> tickets = ticketRepository.findAll();
        return ResponseEntity.ok(tickets);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Ticket> updateTicketStatus(@PathVariable Long id, @RequestParam TicketStatus status) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow(() -> new RuntimeException("Ticket non trovato"));

        User user = getAuthenticatedUser();
        if (!ticket.getUser().equals(user) && !user.getRole().name().equals("ADMIN")) {
            throw new RuntimeException("Accesso negato");
        }

        ticket.setStatus(status);
        ticketRepository.save(ticket);
        return ResponseEntity.ok(ticket);
    }
}
