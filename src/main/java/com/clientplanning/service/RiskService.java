package com.clientplanning.service;

import com.clientplanning.enums.RiskSeverity;
import com.clientplanning.model.*;
import com.clientplanning.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class RiskService {

    @Autowired
    private RiskRepository riskRepository;

    @Autowired
    private ProjectPlanRepository planRepository;

    // Factory Method: all Risk objects are created through this single method
    private Risk makeRisk(String description, RiskSeverity severity,
                          String category, ProjectPlan plan) {
        Risk r = new Risk();
        r.setDescription(description);
        r.setSeverity(severity);
        r.setCategory(category);
        r.setProjectPlan(plan);
        return r;
    }

    // Auto-flag risks based on client profile and plan data
    public List<Risk> flagRisksForPlan(Long planId) {
        ProjectPlan plan = planRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan not found."));

        // Remove old auto-flags before re-generating
        riskRepository.deleteAll(riskRepository.findByProjectPlanPlanId(planId));

        ClientProfile profile = plan.getClientProfile();
        List<Risk> risks = new ArrayList<>();

        // Risk: very unclear requirements
        if (profile.getRequirementClarity() <= 2) {
            risks.add(makeRisk(
                "Requirement clarity is very low — high chance of scope changes during development.",
                RiskSeverity.HIGH, "Requirement", plan));
        } else if (profile.getRequirementClarity() == 3) {
            risks.add(makeRisk(
                "Requirement clarity is moderate — some rework may be expected.",
                RiskSeverity.MEDIUM, "Requirement", plan));
        }

        // Risk: low budget with unclear requirements
        String budget = profile.getBudgetRange().toLowerCase();
        if ((budget.contains("10000") || budget.contains("10k"))
                && profile.getRequirementClarity() <= 3) {
            risks.add(makeRisk(
                "Low budget combined with unclear requirements risks cost overrun.",
                RiskSeverity.CRITICAL, "Budget", plan));
        }

        // Risk: waterfall with low clarity
        if (plan.getMethodology().toLowerCase().contains("waterfall")
                && profile.getRequirementClarity() <= 2) {
            risks.add(makeRisk(
                "Waterfall methodology with unclear requirements increases rework risk.",
                RiskSeverity.HIGH, "Methodology", plan));
        }

        // Risk: monthly reviews with low clarity
        if (profile.getReviewFrequency().name().equals("MONTHLY")
                && profile.getRequirementClarity() <= 2) {
            risks.add(makeRisk(
                "Monthly reviews are too infrequent given the low requirement clarity.",
                RiskSeverity.MEDIUM, "Communication", plan));
        }

        // Risk: high risk tolerance with low budget
        if (profile.getRiskTolerance().equals("HIGH")
                && (budget.contains("10k") || budget.contains("10000"))) {
            risks.add(makeRisk(
                "High risk tolerance with a low budget may lead to unstable decisions.",
                RiskSeverity.MEDIUM, "Budget", plan));
        }

        if (risks.isEmpty()) {
            risks.add(makeRisk(
                "No major risks detected. Project profile appears well-balanced.",
                RiskSeverity.LOW, "General", plan));
        }

        return riskRepository.saveAll(risks);
    }

    // Add a manual risk using the factory method
    public Risk addManualRisk(Long planId, String description,
                              String severity, String category) {
        ProjectPlan plan = planRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan not found."));
        return riskRepository.save(
                makeRisk(description, RiskSeverity.valueOf(severity), category, plan));
    }

    public List<Risk> getRisksByPlan(Long planId) {
        return riskRepository.findByProjectPlanPlanId(planId);
    }

    public List<Risk> getAllRisks() {
        return riskRepository.findAll();
    }

    public void deleteRisk(Long id) {
        riskRepository.deleteById(id);
    }
}
