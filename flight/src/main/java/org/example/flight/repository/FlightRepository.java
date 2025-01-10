package org.example.flight.repository;

import org.example.flight.entity.Flight;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * TODO Class Description
 *
 * @author Olga Zaitseva
 * @since 20.12.2024
 */
@Repository
public interface FlightRepository extends JpaRepository<Flight, Integer> {
    Page<Flight> findAll(Pageable of);
    Flight getFlightByFlightNumber(String flightNumber);
}
