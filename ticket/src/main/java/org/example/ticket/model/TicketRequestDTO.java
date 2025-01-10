package org.example.ticket.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TODO Class Description
 *
 * @author Olga Zaitseva
 * @since 21.12.2024
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketRequestDTO {
    private String userName;
    private String flightNumber;
    private int price;
    private String status;
}
