package com.clientplanning.repository;

import com.clientplanning.model.PlanningRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

// ESHWAR (PES2UG23CS188)
public interface PlanningRecordRepository extends JpaRepository<PlanningRecord, Long> {
    List<PlanningRecord> findByProjectPlanPlanIdOrderByVersionDesc(Long planId);
    List<PlanningRecord> findAllByOrderByDateDesc();
}
