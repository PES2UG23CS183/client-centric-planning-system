package com.clientplanning.service;

import com.clientplanning.model.*;
import com.clientplanning.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

/**
 * ESHWAR (PES2UG23CS188)
 * Manages versioned planning records — stores and retrieves plan snapshots.
 */
@Service
public class PlanningRecordService {

    @Autowired
    private PlanningRecordRepository recordRepository;

    @Autowired
    private ProjectPlanRepository planRepository;

    // Store a versioned snapshot of the current plan state
    public PlanningRecord storeRecord(Long planId) {
        ProjectPlan plan = planRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan not found."));

        List<PlanningRecord> existing =
                recordRepository.findByProjectPlanPlanIdOrderByVersionDesc(planId);
        int nextVersion = existing.isEmpty() ? 1 : existing.get(0).getVersion() + 1;

        PlanningRecord record = new PlanningRecord();
        record.setProjectPlan(plan);
        record.setDate(LocalDate.now());
        record.setVersion(nextVersion);
        record.setSummary(buildSummary(plan));

        return recordRepository.save(record);
    }

    private String buildSummary(ProjectPlan plan) {
        return "Client: " + plan.getClientProfile().getClient().getUser().getName()
                + " | Methodology: " + plan.getMethodology()
                + " | Timeline: " + plan.getTimeline()
                + " | Status: " + plan.getStatus().name()
                + " | Phases: " + plan.getPhases();
    }

    public List<PlanningRecord> getRecordsByPlan(Long planId) {
        return recordRepository.findByProjectPlanPlanIdOrderByVersionDesc(planId);
    }

    public List<PlanningRecord> getAllRecords() {
        return recordRepository.findAllByOrderByDateDesc();
    }

    public void deleteRecord(Long id) {
        recordRepository.deleteById(id);
    }
}
