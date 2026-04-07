package com.clientplanning.model;

import jakarta.persistence.*;

// DELISHA (PES2UG23CS166)
@Entity
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clientId;

    @Column(nullable = false)
    private String organisation;

    @Column(nullable = false)
    private String contactInfo;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Client() {}

    public Long getClientId() { return clientId; }
    public void setClientId(Long clientId) { this.clientId = clientId; }
    public String getOrganisation() { return organisation; }
    public void setOrganisation(String organisation) { this.organisation = organisation; }
    public String getContactInfo() { return contactInfo; }
    public void setContactInfo(String contactInfo) { this.contactInfo = contactInfo; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
