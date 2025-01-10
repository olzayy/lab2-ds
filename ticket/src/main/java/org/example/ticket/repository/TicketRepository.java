package org.example.ticket.repository;

import org.example.ticket.Entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * TODO Class Description
 *
 * @author Olga Zaitseva
 * @since 20.12.2024
 */
@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    List<Ticket> findAllByUserName(String userName);
    Ticket findTicketByTicketUidAndAndUserName(UUID ticketUid, String userName);
    Ticket deleteTicketByTicketUidAndUserName(UUID ticketUid, String userName);
}
