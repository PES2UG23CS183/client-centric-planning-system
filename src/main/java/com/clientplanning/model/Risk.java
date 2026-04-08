package com.clientplanning.model;

import com.clientplanning.enums.RiskSeverity;
import jakarta.persistence.*;

// ESHWAR (PES2UG23CS188)
@Entity
@Table(name = "risks")
public class Risk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long riskId;

    @Column(nullable = false, length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RiskSeverity severity;

    @Column(nullable = false)
    private String category;

    @ManyToOne
    @JoinColumn(name = "plan_id", nullable = false)
    private ProjectPlan projectPlan;

    public Risk() {}

    public Long getRiskId() { return riskId; }
    public void setRiskId(Long riskId) { this.riskId = riskId; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public RiskSeverity getSeverity() { return severity; }
    public void setSeverity(RiskSeverity severity) { this.severity = severity; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public ProjectPlan getProjectPlan() { return projectPlan; }
    public void setProjectPlan(ProjectPlan projectPlan) { this.projectPlan = projectPlan; }
}
