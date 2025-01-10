package org.example.gateway.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * TODO Class Description
 *
 * @author Olga Zaitseva
 * @since 26.12.2024
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BalanceHistoryRequestDTO {
    private Date date;
    private String ticketUid;
    private int balanceDiff;
    private String operationType;
}
