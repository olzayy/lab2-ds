package org.example.ticket.service;

import org.example.ticket.Entity.Ticket;
import org.example.ticket.TicketApplication;
import org.example.ticket.model.ErrorResponse;
import org.example.ticket.model.TicketRequestDTO;
import org.example.ticket.model.TicketResponseDTO;
import org.example.ticket.repository.TicketRepository;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * TODO Class Description
 *
 * @author Olga Zaitseva
 * @since 20.12.2024
 */
@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private TicketApplication ticketApplication;

    public List<TicketResponseDTO> getUserTickets(String username) {
        List<TicketResponseDTO> responseDTOList = new ArrayList<>();
        List<Ticket> tickets = ticketRepository.findAllByUserName(username);
        for (Ticket ticket : tickets) {
            TicketResponseDTO ticketResponseDTO = new TicketResponseDTO(ticket);
            responseDTOList.add(ticketResponseDTO);
        }
        return responseDTOList;
    }

    public TicketResponseDTO getUserTicketByUid(String uid, String username) {
        Ticket ticket = ticketRepository.findTicketByTicketUidAndAndUserName(UUID.fromString(uid), username);
        if (ticket == null) {
            throw new ObjectNotFoundException("Билет не найден", new ErrorResponse());
        }
        else {
            return new TicketResponseDTO(ticket);
        }
    }

    public TicketResponseDTO deleteUserTicketByUid(String uid, String username) {
        Ticket ticket = ticketRepository.findTicketByTicketUidAndAndUserName(UUID.fromString(uid), username);
        ticket.setStatus("CANCELED");
        ticketRepository.save(ticket);
        if (ticket == null) {
            throw new ObjectNotFoundException("Билет не найден", new ErrorResponse());
        }
        else {
            return new TicketResponseDTO(ticket);
        }
    }

    public TicketResponseDTO createUserTicket(String username, TicketRequestDTO ticketRequestDTO) {
        Ticket ticket = new Ticket();
        ticket.setUserName(username);
        ticket.setFlightNumber(ticketRequestDTO.getFlightNumber());
        ticket.setPrice(ticketRequestDTO.getPrice());
        ticket.setStatus(ticketRequestDTO.getStatus());
        ticket.setTicketUid(UUID.randomUUID());
        Ticket result = ticketRepository.save(ticket);
        return new TicketResponseDTO(result);
    }
}
