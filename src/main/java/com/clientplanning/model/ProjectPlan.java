package com.clientplanning.model;

import com.clientplanning.enums.ProjectStatus;
import jakarta.persistence.*;

// DIYA (PES2UG23CS183)
@Entity
@Table(name = "project_plans")
public class ProjectPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long planId;
    @Column(nullable = false, length = 1000)
    private String phases;
    @Column(nullable = false)
    private String timeline;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProjectStatus status;
    @Column(nullable = false)
    private String methodology;
    @Column(length = 500)
    private String executionStrategy;
    @Column(nullable = false)
    private String recommendedReviewFrequency;
    @Column(length = 1000)
    private String decisionSummary;
    @Column(length = 1000)
    private String conflicts;
    @OneToOne
    @JoinColumn(name = "profile_id", nullable = false)
    private ClientProfile clientProfile;
    public ProjectPlan() {}
    public Long getPlanId() { return planId; }
    public void setPlanId(Long planId) { this.planId = planId; }
    public String getPhases() { return phases; }
    public void setPhases(String phases) { this.phases = phases; }
    public String getTimeline() { return timeline; }
    public void setTimeline(String timeline) { this.timeline = timeline; }
    public ProjectStatus getStatus() { return status; }
    public void setStatus(ProjectStatus status) { this.status = status; }
    public String getMethodology() { return methodology; }
    public void setMethodology(String methodology) { this.methodology = methodology; }
    public String getExecutionStrategy() { return executionStrategy; }
    public void setExecutionStrategy(String executionStrategy) { this.executionStrategy = executionStrategy; }
    public String getRecommendedReviewFrequency() { return recommendedReviewFrequency; }
    public void setRecommendedReviewFrequency(String recommendedReviewFrequency) { this.recommendedReviewFrequency = recommendedReviewFrequency; }
    public String getDecisionSummary() { return decisionSummary; }
    public void setDecisionSummary(String decisionSummary) { this.decisionSummary = decisionSummary; }
    public String getConflicts() { return conflicts; }
    public void setConflicts(String conflicts) { this.conflicts = conflicts; }
    public ClientProfile getClientProfile() { return clientProfile; }
    public void setClientProfile(ClientProfile clientProfile) { this.clientProfile = clientProfile; }
}
