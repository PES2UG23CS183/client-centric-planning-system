package com.clientplanning.repository;

import com.clientplanning.model.ClientProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

// DHANYA (PES2UG23CS169)
public interface ClientProfileRepository extends JpaRepository<ClientProfile, Long> {
    List<ClientProfile> findByClientClientId(Long clientId);
}
