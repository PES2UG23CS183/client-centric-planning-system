package com.clientplanning.service;

import com.clientplanning.model.ClientProfile;
import com.clientplanning.model.ProjectPlan;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class DecisionAnalysisService {

    // Analyse decisions made during plan generation and return a summary
    public String analyseDecisions(ClientProfile profile, ProjectPlan plan) {
        StringBuilder summary = new StringBuilder();

        // Step 1: Methodology decision
        summary.append("Methodology selected: ").append(plan.getMethodology()).append(". ");

        // Step 2: Timeline decision
        summary.append("Estimated timeline: ").append(plan.getTimeline()).append(". ");

        // Step 3: Execution strategy decision
        summary.append("Execution strategy: ").append(plan.getExecutionStrategy()).append(". ");

        // Step 4: Review frequency decision
        summary.append("Recommended review frequency: ")
               .append(plan.getRecommendedReviewFrequency()).append(". ");

        // Step 5: Clarity note
        if (profile.getRequirementClarity() <= 2) {
            summary.append("Low requirement clarity detected — additional prototyping phase included.");
        } else {
            summary.append("Requirement clarity is sufficient for direct development.");
        }

        return summary.toString();
    }

    // Identify conflicts between client preferences and plan decisions
    public String identifyConflicts(ClientProfile profile) {
        List<String> conflicts = new ArrayList<>();

        // Conflict: low clarity with waterfall
        if (profile.getRequirementClarity() <= 2
                && profile.getWorkingStyle().name().equals("WATERFALL")) {
            conflicts.add("LOW clarity with WATERFALL — high risk of rework in later phases.");
        }

        // Conflict: low risk tolerance with agile
        if (profile.getRiskTolerance().equals("LOW")
                && profile.getWorkingStyle().name().equals("AGILE")) {
            conflicts.add("LOW risk tolerance with AGILE — frequent changes may conflict with stability needs.");
        }

        // Conflict: very low budget with unclear requirements
        String budget = profile.getBudgetRange().toLowerCase();
        if ((budget.contains("10000") || budget.contains("10k"))
                && profile.getRequirementClarity() <= 2) {
            conflicts.add("LOW budget with UNCLEAR requirements — likely scope creep and cost overrun.");
        }

        // Conflict: monthly review with unclear requirements
        if (profile.getReviewFrequency().name().equals("MONTHLY")
                && profile.getRequirementClarity() <= 2) {
            conflicts.add("MONTHLY reviews with unclear requirements — recommend increasing to WEEKLY.");
        }

        // Conflict: high risk tolerance with low budget
        if (profile.getRiskTolerance().equals("HIGH")
                && (budget.contains("10k") || budget.contains("10000"))) {
            conflicts.add("HIGH risk tolerance with LOW budget — unstable decisions likely.");
        }

        return conflicts.isEmpty()
                ? "No major conflicts detected."
                : String.join(" | ", conflicts);
    }
}
