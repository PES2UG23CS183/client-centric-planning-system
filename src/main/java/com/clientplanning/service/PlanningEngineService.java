package com.clientplanning.service;

import com.clientplanning.enums.ProjectStatus;
import com.clientplanning.model.*;
import com.clientplanning.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PlanningEngineService {

    @Autowired
    private ProjectPlanRepository planRepository;

    @Autowired
    private ClientProfileRepository profileRepository;

    // Injecting DecisionAnalysisService as a separate class (as per class diagram)
    @Autowired
    private DecisionAnalysisService decisionAnalysisService;

    // Main method: generate a project plan from a client profile
    public ProjectPlan generateProjectPlan(Long profileId) {
        ClientProfile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Profile not found."));

        // Update plan if it already exists for this profile
        ProjectPlan plan = planRepository.findByClientProfileProfileId(profileId)
                .orElse(new ProjectPlan());

        plan.setClientProfile(profile);
        plan.setStatus(ProjectStatus.GENERATED);

        // Apply all rules in fixed template order
        applyRules(plan, profile);

        // Delegate to DecisionAnalysisService (separate class)
        plan.setDecisionSummary(decisionAnalysisService.analyseDecisions(profile, plan));
        plan.setConflicts(decisionAnalysisService.identifyConflicts(profile));

        return planRepository.save(plan);
    }

    // Template Method: applies all rules in a fixed sequence
    private void applyRules(ProjectPlan plan, ClientProfile profile) {
        plan.setMethodology(applyMethodologyRule(profile));
        plan.setPhases(applyPhaseRule(profile));
        plan.setTimeline(applyTimelineRule(profile));
        plan.setExecutionStrategy(applyExecutionStrategyRule(profile));
        plan.setRecommendedReviewFrequency(applyReviewFrequencyRule(profile));
    }

    // Rule 1: select methodology based on working style and requirement clarity
    private String applyMethodologyRule(ClientProfile p) {
        switch (p.getWorkingStyle()) {
            case AGILE:
                return p.getRequirementClarity() <= 2
                        ? "Scrum with short sprints (low clarity — frequent feedback needed)"
                        : "Agile / Scrum";
            case WATERFALL:
                return p.getRequirementClarity() >= 4
                        ? "Waterfall (requirements are well defined)"
                        : "Waterfall with review checkpoints (unclear requirements — checkpoints added)";
            case HYBRID:
                return "Hybrid — Agile sprints for development, Waterfall gates for delivery";
            default:
                return "Agile";
        }
    }

    // Rule 2: determine project phases based on methodology and clarity
    private String applyPhaseRule(ClientProfile p) {
        List<String> phases = new ArrayList<>();
        phases.add("Requirements Gathering");
        if (p.getRequirementClarity() <= 2) {
            phases.add("Prototyping and Validation");
        }
        phases.add("System Design");
        phases.add("Implementation");
        if (!p.getWorkingStyle().name().equals("WATERFALL")) {
            phases.add("Sprint Reviews");
        }
        phases.add("Testing and QA");
        phases.add("Deployment");
        phases.add("Post-Deployment Review");
        return String.join(", ", phases);
    }

    // Rule 3: estimate timeline based on budget and clarity
    private String applyTimelineRule(ClientProfile p) {
        String budget = p.getBudgetRange().toLowerCase();
        int clarity = p.getRequirementClarity();
        if (budget.contains("lakh") || budget.contains("100000")) {
            return clarity >= 4 ? "4–6 months" : "6–9 months (low clarity adds buffer)";
        } else if (budget.contains("50000") || budget.contains("50k")) {
            return clarity >= 3 ? "2–4 months" : "3–5 months";
        } else {
            return clarity >= 4 ? "1–3 months" : "2–4 months";
        }
    }

    // Rule 4: execution strategy based on risk tolerance
    private String applyExecutionStrategyRule(ClientProfile p) {
        switch (p.getRiskTolerance()) {
            case "HIGH":   return "Parallel workstreams — multiple features developed simultaneously";
            case "LOW":    return "Sequential delivery — one phase fully validated before the next";
            default:       return "Incremental delivery — phased releases with client sign-off";
        }
    }

    // Rule 5: recommended review frequency
    private String applyReviewFrequencyRule(ClientProfile p) {
        if (p.getRequirementClarity() <= 2) return "WEEKLY (low clarity requires frequent sync)";
        switch (p.getReviewFrequency()) {
            case WEEKLY:         return "WEEKLY";
            case BIWEEKLY:       return "BIWEEKLY";
            case MONTHLY:        return "MONTHLY";
            default:             return "MILESTONE_BASED";
        }
    }

    // Update plan status
    public ProjectPlan updateStatus(Long planId, String status) {
        ProjectPlan plan = planRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan not found."));
        plan.setStatus(ProjectStatus.valueOf(status));
        return planRepository.save(plan);
    }

    public List<ProjectPlan> getAllPlans() {
        return planRepository.findAllByOrderByPlanIdDesc();
    }

    public Optional<ProjectPlan> getPlanById(Long id) {
        return planRepository.findById(id);
    }

    public Optional<ProjectPlan> getPlanByProfileId(Long profileId) {
        return planRepository.findByClientProfileProfileId(profileId);
    }

    public void deletePlan(Long id) {
        planRepository.deleteById(id);
    }
}
