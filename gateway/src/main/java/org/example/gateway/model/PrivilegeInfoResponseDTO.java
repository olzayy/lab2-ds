package org.example.gateway.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * TODO Class Description
 *
 * @author Olga Zaitseva
 * @since 21.12.2024
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrivilegeInfoResponseDTO {
    private String balance;
    private String status;
    private List<BalanceHistoryDTO> history;
}
