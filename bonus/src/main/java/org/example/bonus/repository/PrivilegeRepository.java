package org.example.bonus.repository;

import org.example.bonus.entity.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * TODO Class Description
 *
 * @author Olga Zaitseva
 * @since 21.12.2024
 */
@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, Integer> {
    Privilege findPrivilegeByUserName(String userName);
}
