package org.example.gateway.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * TODO Class Description
 *
 * @author Olga Zaitseva
 * @since 10.01.2025
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoResponseDTO {
    private List<TicketResponseDTO> tickets;
    private PrivilegeDTO privilege;
}
