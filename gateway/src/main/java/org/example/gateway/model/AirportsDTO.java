package org.example.gateway.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TODO Class Description
 *
 * @author Olga Zaitseva
 * @since 20.12.2024
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AirportsDTO {
    private String toAirportName;
    private String fromAirportName;
    private String toAirportCity;
    private String fromAirportCity;
    private String toAirportCountry;
    private String fromAirportCountry;
}
