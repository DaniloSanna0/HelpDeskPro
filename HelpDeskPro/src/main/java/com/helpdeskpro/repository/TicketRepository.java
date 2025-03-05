package com.helpdeskpro.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.helpdeskpro.model.Ticket;
import com.helpdeskpro.model.User;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByUserId(Long userId);

	List<Ticket> findByUser(User user);
}