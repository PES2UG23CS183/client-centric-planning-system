package com.clientplanning.repository;

import com.clientplanning.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// DELISHA (PES2UG23CS166)
public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByUserUserId(Long userId);
}
