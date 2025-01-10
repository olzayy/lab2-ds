package org.example.gateway.service;

import org.example.gateway.model.AirportsDTO;
import org.example.gateway.model.FlightResponseDTO;
import org.example.gateway.model.PaginationResponseDTO;
import org.example.gateway.model.TicketResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * TODO Class Description
 *
 * @author Olga Zaitseva
 * @since 20.12.2024
 */
@Service
public class FlightService {

    @Value("${flight.service.url}")
    private String basicUrl;

    public PaginationResponseDTO getFlights(Integer page, Integer size) throws URISyntaxException {
        URI uri = new URI(basicUrl + "/flights/" + page + "/" + size);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<PaginationResponseDTO> result = restTemplate.exchange(
                uri, HttpMethod.GET, entity, PaginationResponseDTO.class
        );
        return result.getBody();
    }

    public FlightResponseDTO getFlight(String flightNumber) throws URISyntaxException {
        URI uri = new URI(basicUrl + "/flights/" + flightNumber);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<FlightResponseDTO> result = restTemplate.exchange(
                uri, HttpMethod.GET, entity, FlightResponseDTO.class
        );
        return result.getBody();
    }

    public AirportsDTO getAirportsInfo(String flightNumber) throws URISyntaxException {
        URI uri = new URI(basicUrl + "/airports/" + flightNumber);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<AirportsDTO> result = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                entity,
                AirportsDTO.class);
        return result.getBody();
    }
}
