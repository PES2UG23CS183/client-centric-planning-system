package com.clientplanning.model;

import com.clientplanning.enums.*;
import jakarta.persistence.*;

// DHANYA (PES2UG23CS169)
@Entity
@Table(name = "client_profiles")
public class ClientProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profileId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WorkingStyle workingStyle;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CommunicationMode communication;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReviewFrequency reviewFrequency;

    @Column(name = "constraints", length = 500)
    private String constraints;

    @Column(nullable = false)
    private String budgetRange;

    @Column(nullable = false)
    private int requirementClarity;

    @Column(nullable = false)
    private String riskTolerance;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    public ClientProfile() {}

    public Long getProfileId() { return profileId; }
    public void setProfileId(Long profileId) { this.profileId = profileId; }
    public WorkingStyle getWorkingStyle() { return workingStyle; }
    public void setWorkingStyle(WorkingStyle workingStyle) { this.workingStyle = workingStyle; }
    public CommunicationMode getCommunication() { return communication; }
    public void setCommunication(CommunicationMode communication) { this.communication = communication; }
    public ReviewFrequency getReviewFrequency() { return reviewFrequency; }
    public void setReviewFrequency(ReviewFrequency reviewFrequency) { this.reviewFrequency = reviewFrequency; }
    public String getConstraints() { return constraints; }
    public void setConstraints(String constraints) { this.constraints = constraints; }
    public String getBudgetRange() { return budgetRange; }
    public void setBudgetRange(String budgetRange) { this.budgetRange = budgetRange; }
    public int getRequirementClarity() { return requirementClarity; }
    public void setRequirementClarity(int requirementClarity) { this.requirementClarity = requirementClarity; }
    public String getRiskTolerance() { return riskTolerance; }
    public void setRiskTolerance(String riskTolerance) { this.riskTolerance = riskTolerance; }
    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }
}