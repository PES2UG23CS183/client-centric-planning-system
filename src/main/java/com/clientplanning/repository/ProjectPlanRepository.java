package com.clientplanning.repository;

import com.clientplanning.model.ProjectPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

// DIYA (PES2UG23CS183)
public interface ProjectPlanRepository extends JpaRepository<ProjectPlan, Long> {
    Optional<ProjectPlan> findByClientProfileProfileId(Long profileId);
    List<ProjectPlan> findAllByOrderByPlanIdDesc();
}
