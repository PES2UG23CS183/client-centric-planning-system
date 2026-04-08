package com.clientplanning.model;

import jakarta.persistence.*;
import java.time.LocalDate;

// ESHWAR (PES2UG23CS188)
@Entity
@Table(name = "planning_records")
public class PlanningRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recordId;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private int version;

    @Column(length = 1000)
    private String summary;

    @ManyToOne
    @JoinColumn(name = "plan_id", nullable = false)
    private ProjectPlan projectPlan;

    public PlanningRecord() {}

    public Long getRecordId() { return recordId; }
    public void setRecordId(Long recordId) { this.recordId = recordId; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public int getVersion() { return version; }
    public void setVersion(int version) { this.version = version; }
    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }
    public ProjectPlan getProjectPlan() { return projectPlan; }
    public void setProjectPlan(ProjectPlan projectPlan) { this.projectPlan = projectPlan; }
}
