package org.example.gateway.controller;

import org.example.gateway.model.*;
import org.example.gateway.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.List;

/**
 * TODO Class Description
 *
 * @author Olga Zaitseva
 * @since 20.12.2024
 */
@RestController
@RequestMapping("/api/v1/")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @GetMapping("/tickets")
    public ResponseEntity<List<TicketResponseDTO>> getUserTickets (
            @RequestHeader("X-User-Name") String username
    ) throws URISyntaxException {
        List<TicketResponseDTO> ticketsDTO = ticketService.getUserTicketDTOList(username);
        return ResponseEntity.ok(ticketsDTO);
    }

    @GetMapping("/tickets/{ticketUid}")
    public ResponseEntity<?> getUserTicket (
            @RequestHeader("X-User-Name") String username,
            @PathVariable String ticketUid
    ) throws URISyntaxException {
        try {
            TicketResponseDTO result = ticketService.getUserTicketDTO(username, ticketUid);
            return ResponseEntity.ok(result);
        }
        catch (ErrorResponse e) {
            return ResponseEntity.status(404).body(new MessageDTO("Билет не найден"));
        }
    }

    @DeleteMapping("/tickets/{ticketUid}")
    public ResponseEntity<?> deleteUserTicket (
            @RequestHeader("X-User-Name") String username,
            @PathVariable String ticketUid
    ) throws URISyntaxException {
        try {
            ticketService.deleteUserTicket(username, ticketUid);
            return ResponseEntity.status(204).build();
        }
        catch (ErrorResponse e) {
            return ResponseEntity.status(404).body(new MessageDTO("Билет не найден"));
        }
    }

    @PostMapping("/tickets")
    public ResponseEntity<?> createUserTicket (
            @RequestHeader("X-User-Name") String username,
            @RequestBody TicketPurchaseRequestDTO request
    ) throws URISyntaxException, ErrorResponse {
        try {
            TicketPurchaseResponseDTO response = ticketService.buyTicket(username, request);
            return ResponseEntity.ok(response);
        }
        catch (ValidationErrorResponse e) {
            return ResponseEntity.badRequest().body(e);
        }
    }
}
