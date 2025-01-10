package org.example.gateway.model;

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
public class TicketPurchaseResponseDTO {
    private String ticketUid;
    private String flightNumber;
    private String fromAirport;
    private String toAirport;
    private String date;
    private int price;
    private int paidByMoney;
    private int paidByBonuses;
    private String status;
    private PrivilegeDTO privilege;
}
