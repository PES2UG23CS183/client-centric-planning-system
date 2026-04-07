package com.clientplanning.model;

import jakarta.persistence.*;

@Entity
@Table(name = "alignment_evaluations")
public class AlignmentEvaluator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long evaluationId;

    @Column(nullable = false)
    private double alignmentScore;

    @Column(length = 1000)
    private String scoreBreakdown;

    @Column(nullable = false)
    private String consistencyVerdict;

    @OneToOne
    @JoinColumn(name = "plan_id", nullable = false)
    private ProjectPlan projectPlan;

    public AlignmentEvaluator() {}

    public Long getEvaluationId() { return evaluationId; }
    public void setEvaluationId(Long evaluationId) { this.evaluationId = evaluationId; }
    public double getAlignmentScore() { return alignmentScore; }
    public void setAlignmentScore(double alignmentScore) { this.alignmentScore = alignmentScore; }
    public String getScoreBreakdown() { return scoreBreakdown; }
    public void setScoreBreakdown(String scoreBreakdown) { this.scoreBreakdown = scoreBreakdown; }
    public String getConsistencyVerdict() { return consistencyVerdict; }
    public void setConsistencyVerdict(String consistencyVerdict) { this.consistencyVerdict = consistencyVerdict; }
    public ProjectPlan getProjectPlan() { return projectPlan; }
    public void setProjectPlan(ProjectPlan projectPlan) { this.projectPlan = projectPlan; }
}
