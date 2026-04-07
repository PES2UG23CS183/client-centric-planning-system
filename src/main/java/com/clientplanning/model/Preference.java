package com.clientplanning.model;

import com.clientplanning.enums.WorkingStyle;
import jakarta.persistence.*;

// DHANYA (PES2UG23CS169)
@Entity
@Table(name = "preferences")
public class Preference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long preferenceId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WorkingStyle developmentApproach;

    @Column(nullable = false)
    private String executionStrategy;

    @Column(nullable = false)
    private String reviewProcess;

    @OneToOne
    @JoinColumn(name = "profile_id", nullable = false)
    private ClientProfile clientProfile;

    public Preference() {}

    public Long getPreferenceId() { return preferenceId; }
    public void setPreferenceId(Long preferenceId) { this.preferenceId = preferenceId; }
    public WorkingStyle getDevelopmentApproach() { return developmentApproach; }
    public void setDevelopmentApproach(WorkingStyle developmentApproach) { this.developmentApproach = developmentApproach; }
    public String getExecutionStrategy() { return executionStrategy; }
    public void setExecutionStrategy(String executionStrategy) { this.executionStrategy = executionStrategy; }
    public String getReviewProcess() { return reviewProcess; }
    public void setReviewProcess(String reviewProcess) { this.reviewProcess = reviewProcess; }
    public ClientProfile getClientProfile() { return clientProfile; }
    public void setClientProfile(ClientProfile clientProfile) { this.clientProfile = clientProfile; }
}
