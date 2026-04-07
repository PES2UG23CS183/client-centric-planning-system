package com.clientplanning.repository;

import com.clientplanning.model.Preference;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// DHANYA (PES2UG23CS169)
public interface PreferenceRepository extends JpaRepository<Preference, Long> {
    Optional<Preference> findByClientProfileProfileId(Long profileId);
}
