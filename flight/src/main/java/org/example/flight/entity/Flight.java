package org.example.flight.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

/**
 * TODO Class Description
 *
 * @author Olga Zaitseva
 * @since 20.12.2024
 */
@Entity
@Table(name="flight")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "flight_number", nullable = false)
    private String flightNumber;

    @Column(name = "datetime", nullable = false)
    private OffsetDateTime dateTime;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "from_airport_id", referencedColumnName = "id")
    private Airport fromAirport;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "to_airport_id", referencedColumnName = "id")
    private Airport toAirport;

    @Column(name = "price", nullable = false)
    private Integer price;
}
