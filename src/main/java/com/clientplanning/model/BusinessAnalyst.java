package com.clientplanning.model;

import jakarta.persistence.*;

// DELISHA (PES2UG23CS166)
@Entity
@Table(name = "business_analysts")
public class BusinessAnalyst {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long analystId;

    @Column(nullable = false)
    private String specialisation;

    @Column(nullable = false)
    private int experience;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public BusinessAnalyst() {}

    public Long getAnalystId() { return analystId; }
    public void setAnalystId(Long analystId) { this.analystId = analystId; }
    public String getSpecialisation() { return specialisation; }
    public void setSpecialisation(String specialisation) { this.specialisation = specialisation; }
    public int getExperience() { return experience; }
    public void setExperience(int experience) { this.experience = experience; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
