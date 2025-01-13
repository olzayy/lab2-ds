package org.example.flight.controller;

import org.example.flight.entity.Flight;
import org.example.flight.model.AirportsDTO;
import org.example.flight.model.FlightResponseDTO;
import org.example.flight.model.PaginationResponseDTO;
import org.example.flight.repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO Class Description
 *
 * @author Olga Zaitseva
 * @since 20.12.2024
 */
@RestController
@RequestMapping
public class FlightController {

    @Autowired
    private FlightRepository flightRepository;

    @GetMapping("/flights/{page}/{size}")
    public ResponseEntity<PaginationResponseDTO> getFlights(@PathVariable int page, @PathVariable int size) {
        List<Flight> flights = flightRepository.findAll(PageRequest.of(setPage(page), size)).getContent();
        PaginationResponseDTO paginationResponseDTO = getFlightsPagination(page, size, flights);
        return ResponseEntity.ok(paginationResponseDTO);
    }

    @GetMapping("/flights/{flightNumber}")
    public ResponseEntity<FlightResponseDTO> getFlight(@PathVariable String flightNumber) {
        Flight flight = flightRepository.getFlightByFlightNumber(flightNumber);
        if (flight == null) {
            return ResponseEntity.ok(null);
        }
        FlightResponseDTO response = new FlightResponseDTO(flight);
        return ResponseEntity.ok(response);
    }

    private PaginationResponseDTO getFlightsPagination(int page, int size, List<Flight> flights){
        List<FlightResponseDTO> flightResponseDTOList = new ArrayList<>();
        for(Flight flight: flights){
            FlightResponseDTO flightResp = new FlightResponseDTO(flight);
            flightResp.setFromAirport(flight.getFromAirport().getCity() + " " + flight.getFromAirport().getName());
            flightResp.setToAirport(flight.getToAirport().getCity() + " " + flight.getToAirport().getName());
            flightResponseDTOList.add(flightResp);
        }
        return new PaginationResponseDTO(page, size, flightResponseDTOList.size(), flightResponseDTOList);
    }

    @GetMapping("/airports/{flightNumber}")
    public ResponseEntity<AirportsDTO> getAirportsInfo(@PathVariable String flightNumber) {
        Flight flight = flightRepository.getFlightByFlightNumber(flightNumber);
        AirportsDTO airportsDTO = new AirportsDTO();
        airportsDTO.setToAirportName(flight.getToAirport().getName());
        airportsDTO.setFromAirportName(flight.getFromAirport().getName());
        airportsDTO.setToAirportCity(flight.getToAirport().getCity());
        airportsDTO.setFromAirportCity(flight.getFromAirport().getCity());
        airportsDTO.setToAirportCountry(flight.getToAirport().getCountry());
        airportsDTO.setFromAirportCountry(flight.getFromAirport().getCountry());
        return ResponseEntity.ok(airportsDTO);
    }

    private int setPage(int page){
        if (page == 0){
            return 0;
        }
        return page-1;
    }
}
