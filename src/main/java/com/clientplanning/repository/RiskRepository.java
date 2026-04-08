package com.clientplanning.repository;

import com.clientplanning.model.Risk;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

// ESHWAR (PES2UG23CS188)
public interface RiskRepository extends JpaRepository<Risk, Long> {
    List<Risk> findByProjectPlanPlanId(Long planId);
}
