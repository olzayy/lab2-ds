package org.example.flight.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.flight.entity.Flight;

import java.time.format.DateTimeFormatter;

/**
 * TODO Class Description
 *
 * @author Olga Zaitseva
 * @since 20.12.2024
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlightResponseDTO {
    private String flightNumber;
    private String fromAirport;
    private String toAirport;
    private String date;
    private int price;

    public FlightResponseDTO(Flight flight){
        this.flightNumber = flight.getFlightNumber();
        this.fromAirport = flight.getFromAirport().getName();
        this.toAirport = flight.getToAirport().getName();
        this.date = flight.getDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.price = flight.getPrice();
    }
}
