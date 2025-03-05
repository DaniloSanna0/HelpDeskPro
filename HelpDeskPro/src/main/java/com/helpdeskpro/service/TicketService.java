package com.helpdeskpro.service;

import com.helpdeskpro.model.Ticket;
import com.helpdeskpro.repository.TicketRepository;
import org.springframework.stereotype.Service;
import com.helpdeskpro.model.TicketStatus;

import java.util.List;
import java.util.Optional;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    public void updateTicketStatus(Long id, TicketStatus status) {
        Optional<Ticket> ticketOpt = ticketRepository.findById(id);
        if (ticketOpt.isPresent()) {
            Ticket ticket = ticketOpt.get();
            ticket.setStatus(status); 
            ticketRepository.save(ticket);
        }
    }


    public void deleteTicket(Long id) {
        ticketRepository.deleteById(id);
    }
}
