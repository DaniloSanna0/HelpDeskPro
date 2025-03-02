package com.helpdeskpro.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.helpdeskpro.model.Ticket;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByUserId(Long userId);
}