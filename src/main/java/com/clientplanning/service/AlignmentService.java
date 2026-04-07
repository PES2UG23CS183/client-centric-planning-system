package com.clientplanning.service;

import com.clientplanning.model.*;
import com.clientplanning.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AlignmentService {

    @Autowired
    private AlignmentEvaluatorRepository alignmentRepository;

    @Autowired
    private ProjectPlanRepository planRepository;

    // Calculate and save alignment score for a given plan
    public AlignmentEvaluator calculateAlignment(Long planId) {
        ProjectPlan plan = planRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan not found."));

        ClientProfile profile = plan.getClientProfile();

        AlignmentEvaluator eval = alignmentRepository
                .findByProjectPlanPlanId(planId)
                .orElse(new AlignmentEvaluator());

        eval.setProjectPlan(plan);

        double score = 0;
        List<String> breakdown = new ArrayList<>();

        // Factor 1: Working style matches methodology (25 pts)
        String methodology = plan.getMethodology().toLowerCase();
        String workingStyle = profile.getWorkingStyle().name().toLowerCase();
        if (methodology.contains(workingStyle)
                || (workingStyle.equals("hybrid")
                    && (methodology.contains("agile") || methodology.contains("waterfall")))) {
            score += 25;
            breakdown.add("+25: Working style matches plan methodology");
        } else {
            breakdown.add("+0: Working style does not match methodology");
        }

        // Factor 2: Requirement clarity (20 pts)
        double clarityScore = (profile.getRequirementClarity() / 5.0) * 20;
        score += clarityScore;
        breakdown.add("+" + (int) clarityScore
                + ": Requirement clarity (" + profile.getRequirementClarity() + "/5)");

        // Factor 3: Review frequency match (20 pts)
        String recFreq = plan.getRecommendedReviewFrequency().toLowerCase();
        String clientFreq = profile.getReviewFrequency().name().toLowerCase();
        if (recFreq.contains(clientFreq)) {
            score += 20;
            breakdown.add("+20: Review frequency matches client preference");
        } else {
            score += 10;
            breakdown.add("+10: Review frequency partially matches");
        }

        // Factor 4: Risk tolerance matches execution strategy (20 pts)
        String riskTolerance = profile.getRiskTolerance().toLowerCase();
        String execStrategy = plan.getExecutionStrategy() != null
                ? plan.getExecutionStrategy().toLowerCase() : "";
        if ((riskTolerance.equals("low") && execStrategy.contains("sequential"))
                || (riskTolerance.equals("high") && execStrategy.contains("parallel"))
                || (riskTolerance.equals("medium") && execStrategy.contains("incremental"))) {
            score += 20;
            breakdown.add("+20: Risk tolerance matches execution strategy");
        } else {
            score += 10;
            breakdown.add("+10: Risk tolerance partially consistent with strategy");
        }

        // Factor 5: No conflicts detected (15 pts)
        String conflicts = plan.getConflicts();
        if (conflicts == null || conflicts.equals("No major conflicts detected.")) {
            score += 15;
            breakdown.add("+15: No conflicts detected");
        } else {
            breakdown.add("+0: Conflicts present — reduces alignment score");
        }

        eval.setAlignmentScore(Math.min(score, 100));
        eval.setScoreBreakdown(String.join("\n", breakdown));

        if (score >= 80) {
            eval.setConsistencyVerdict("High — Plan is well aligned with client expectations.");
        } else if (score >= 50) {
            eval.setConsistencyVerdict("Medium — Moderate alignment. Review flagged areas.");
        } else {
            eval.setConsistencyVerdict("Low — Significant mismatches between plan and client profile.");
        }

        return alignmentRepository.save(eval);
    }

    public Optional<AlignmentEvaluator> getAlignmentByPlanId(Long planId) {
        return alignmentRepository.findByProjectPlanPlanId(planId);
    }

    public List<AlignmentEvaluator> getAllAlignments() {
        return alignmentRepository.findAll();
    }
}
