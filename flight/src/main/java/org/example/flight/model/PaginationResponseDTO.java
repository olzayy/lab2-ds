package org.example.flight.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * TODO Class Description
 *
 * @author Olga Zaitseva
 * @since 20.12.2024
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginationResponseDTO {
    private int page;
    private int pageSize;
    private int totalElements;
    private List<FlightResponseDTO> items;
}
