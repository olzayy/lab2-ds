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
public class PrivilegeDTO {
    private int balance;
    private String status;
}
