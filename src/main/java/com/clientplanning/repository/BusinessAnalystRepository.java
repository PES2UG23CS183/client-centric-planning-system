package com.clientplanning.repository;

import com.clientplanning.model.BusinessAnalyst;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// DELISHA (PES2UG23CS166)
public interface BusinessAnalystRepository extends JpaRepository<BusinessAnalyst, Long> {
    Optional<BusinessAnalyst> findByUserUserId(Long userId);
}
