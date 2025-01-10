package org.example.ticket.controller;

import org.example.ticket.Entity.Ticket;
import org.example.ticket.model.TicketPurchaseResponseDTO;
import org.example.ticket.model.TicketRequestDTO;
import org.example.ticket.model.TicketResponseDTO;
import org.example.ticket.service.TicketService;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * TODO Class Description
 *
 * @author Olga Zaitseva
 * @since 20.12.2024
 */
@RestController
@RequestMapping
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @GetMapping("/tickets")
    public ResponseEntity<List<TicketResponseDTO>> getAllUserTickets(@RequestHeader("X-User-Name") String username) {
        List<TicketResponseDTO> result = ticketService.getUserTickets(username);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/tickets")
    public ResponseEntity<TicketResponseDTO> createUserTicket(
            @RequestHeader("X-User-Name") String username,
            @RequestBody TicketRequestDTO ticketRequestDTO) {
        TicketResponseDTO response = ticketService.createUserTicket(username, ticketRequestDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/tickets/{ticketUid}")
    public ResponseEntity<TicketResponseDTO> getUserTicket(
            @RequestHeader("X-User-Name") String username,
            @PathVariable("ticketUid") String ticketUid
    ) {
        try {
            TicketResponseDTO result = ticketService.getUserTicketByUid(ticketUid, username);
            return ResponseEntity.ok(result);
        }catch (ObjectNotFoundException e){
            return  ResponseEntity.status(404).body(null);
        }
    }

    @DeleteMapping("/tickets/{ticketUid}")
    public ResponseEntity<TicketResponseDTO> deleteUserTicket(
            @RequestHeader("X-User-Name") String username,
            @PathVariable("ticketUid") String ticketUid
    ) {
        try {
            TicketResponseDTO result = ticketService.deleteUserTicketByUid(ticketUid, username);
            return ResponseEntity.ok(result);
        }catch (ObjectNotFoundException e){
            return  ResponseEntity.status(404).body(null);
        }
    }
}
