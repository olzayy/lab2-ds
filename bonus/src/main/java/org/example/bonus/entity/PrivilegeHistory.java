package org.example.bonus.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * TODO Class Description
 *
 * @author Olga Zaitseva
 * @since 21.12.2024
 */
@Entity
@Table(name="privilege_history")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PrivilegeHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name="privilege_id")
    private Privilege privilege;

    @Column(name = "ticket_uid", nullable = false)
    private UUID ticketUid;

    @Column(name = "datetime", nullable = false)
    private OffsetDateTime dateTime;

    @Column(name = "balance_diff", nullable = false)
    private Integer balanceDiff;

    @Column(name = "operation_type", nullable = false)
    private String operationType;
}
