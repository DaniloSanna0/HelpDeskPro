package com.helpdeskpro.controller;

import java.util.Map;
import com.helpdeskpro.model.Ticket;
import com.helpdeskpro.model.TicketStatus;
import com.helpdeskpro.service.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:5173")
public class AdminController {
    
    private final TicketService ticketService;

    public AdminController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("/tickets")
    public ResponseEntity<List<Ticket>> getAllTickets() {
        return ResponseEntity.ok(ticketService.getAllTickets());
    }

    @PutMapping("/tickets/{id}")
    public ResponseEntity<?> updateTicketStatus(@PathVariable Long id, @RequestBody Map<String, String> request) {
        String statusStr = request.get("status");
        TicketStatus status;
        try {
            status = TicketStatus.valueOf(statusStr.toUpperCase()); // Converte la stringa in ENUM
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Stato non valido");
        }

        ticketService.updateTicketStatus(id, status);
        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/tickets/{id}")
    public ResponseEntity<?> deleteTicket(@PathVariable Long id) {
        ticketService.deleteTicket(id);
        return ResponseEntity.ok().build();
    }
}
