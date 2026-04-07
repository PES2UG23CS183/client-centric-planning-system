package com.clientplanning.repository;

import com.clientplanning.model.AlignmentEvaluator;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// ESHWAR (PES2UG23CS188)
public interface AlignmentEvaluatorRepository extends JpaRepository<AlignmentEvaluator, Long> {
    Optional<AlignmentEvaluator> findByProjectPlanPlanId(Long planId);
}
