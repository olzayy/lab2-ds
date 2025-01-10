package org.example.gateway.controller;

import org.example.gateway.model.PaginationResponseDTO;
import org.example.gateway.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;

/**
 * Контроллер для перелетов
 *
 * @author Olga Zaitseva
 * @since 20.12.2024
 */
@RestController
@RequestMapping("/api/v1/")
public class FlightController {

    @Autowired
    private FlightService flightService;

    @GetMapping("/flights")
    public ResponseEntity<?> getAllFlights(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        try {
            PaginationResponseDTO paginationResponseDTO = flightService.getFlights(page, size);
            return ResponseEntity.ok(paginationResponseDTO);
        }
        catch (URISyntaxException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
