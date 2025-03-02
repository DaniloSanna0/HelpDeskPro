package com.helpdeskpro.controller;

import com.helpdeskpro.model.Ticket;
import com.helpdeskpro.model.TicketStatus;
import com.helpdeskpro.model.User;
import com.helpdeskpro.repository.TicketRepository;
import com.helpdeskpro.repository.UserRepository;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    public TicketController(TicketRepository ticketRepository, UserRepository userRepository) {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<Ticket> createTicket(@Valid @RequestBody Ticket ticket) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        ticket.setUser(user);
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setCreatedAt(LocalDateTime.now());  
        ticketRepository.save(ticket);

        return ResponseEntity.ok(ticket);
    }

    // Ottieni i ticket dell'utente autenticato
    @GetMapping
    public ResponseEntity<List<Ticket>> getMyTickets() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("Utente non trovato"));

        List<Ticket> tickets = ticketRepository.findByUserId(user.getId());
        return ResponseEntity.ok(tickets);
    }

    // Solo Admin può vedere tutti i ticket
    @GetMapping("/all")
    public ResponseEntity<List<Ticket>> getAllTickets() {
        List<Ticket> tickets = ticketRepository.findAll();
        return ResponseEntity.ok(tickets);
    }

    // Modifica lo stato di un ticket (Solo Admin)
    @PutMapping("/{id}/status")
    public ResponseEntity<Ticket> updateTicketStatus(@PathVariable Long id, @RequestParam TicketStatus status) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow(() -> new RuntimeException("Ticket non trovato"));
        ticket.setStatus(status);
        ticketRepository.save(ticket);
        return ResponseEntity.ok(ticket);
    }
}
