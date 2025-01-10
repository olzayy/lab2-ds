package org.example.ticket.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * TODO Class Description
 *
 * @author Olga Zaitseva
 * @since 20.12.2024
 */
@Entity
@Table(name="ticket")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "ticket_uid", nullable = false, unique = true)
    private UUID ticketUid;

    @Column(name = "username", nullable = false)
    private String userName;

    @Column(name = "flight_number", nullable = false)
    private String flightNumber;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "status", nullable = false)
    private String status;
}
