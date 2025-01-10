package org.example.bonus.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * TODO Class Description
 *
 * @author Olga Zaitseva
 * @since 21.12.2024
 */
@Entity
@Table(name="privilege")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Privilege {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "username", nullable = false, unique = true)
    private String userName;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "balance")
    private Integer balance;

    @OneToMany(mappedBy = "privilege", fetch = FetchType.EAGER)
    private List<PrivilegeHistory> privilegeHistoryList;
}
