package com.clientplanning.service;

import com.clientplanning.enums.*;
import com.clientplanning.model.*;
import com.clientplanning.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * DHANYA (PES2UG23CS169)
 *
 * GRASP Principle Applied: Creator
 *   ClientProfileService is responsible for creating ClientProfile and
 *   Preference objects. According to the Creator principle, a class
 *   should create instances of another class if it closely uses or
 *   contains that data. Since this service aggregates all the data
 *   needed to build a ClientProfile, it is the natural creator.
 *
 * Design Pattern Applied: Strategy Pattern
 *   The execution strategy for a project is selected at runtime based
 *   on the client's risk tolerance. Different strategy strings are
 *   assigned dynamically (sequential, incremental, parallel) — this
 *   mirrors the Strategy pattern where behaviour is chosen at runtime
 *   based on context, without changing the calling code.
 */
@Service
public class ClientProfileService {

    @Autowired
    private ClientProfileRepository profileRepository;

    @Autowired
    private PreferenceRepository preferenceRepository;

    @Autowired
    private ClientRepository clientRepository;

    // GRASP Creator: this service creates ClientProfile objects
    public ClientProfile createProfile(Long clientId, String workingStyle, String communication,
                                       String reviewFrequency, String constraint, String budgetRange,
                                       int requirementClarity, String riskTolerance) {

        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found."));

        ClientProfile profile = new ClientProfile();
        profile.setClient(client);
        profile.setWorkingStyle(WorkingStyle.valueOf(workingStyle));
        profile.setCommunication(CommunicationMode.valueOf(communication));
        profile.setReviewFrequency(ReviewFrequency.valueOf(reviewFrequency));
        profile.setConstraints(constraint);
        profile.setBudgetRange(budgetRange);
        profile.setRequirementClarity(requirementClarity);
        profile.setRiskTolerance(riskTolerance);

        return profileRepository.save(profile);
    }

    // Update an existing profile
    public ClientProfile updateProfile(Long profileId, String workingStyle, String communication,
                                       String reviewFrequency, String constraint, String budgetRange,
                                       int requirementClarity, String riskTolerance) {

        ClientProfile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Profile not found."));

        profile.setWorkingStyle(WorkingStyle.valueOf(workingStyle));
        profile.setCommunication(CommunicationMode.valueOf(communication));
        profile.setReviewFrequency(ReviewFrequency.valueOf(reviewFrequency));
        profile.setConstraints(constraint);
        profile.setBudgetRange(budgetRange);
        profile.setRequirementClarity(requirementClarity);
        profile.setRiskTolerance(riskTolerance);

        return profileRepository.save(profile);
    }

    // Strategy Pattern: execution strategy is selected at runtime based on risk tolerance
    public String selectExecutionStrategy(String riskTolerance) {
        switch (riskTolerance) {
            case "HIGH":
                return "Parallel workstreams — multiple features developed simultaneously";
            case "LOW":
                return "Sequential delivery — one phase fully validated before the next";
            default:
                return "Incremental delivery — phased releases with client sign-off at each stage";
        }
    }

    // GRASP Creator: this service also creates Preference objects
    public Preference savePreference(Long profileId, String developmentApproach,
                                     String executionStrategy, String reviewProcess) {

        ClientProfile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Profile not found."));

        // Update if preference already exists for this profile
        Preference pref = preferenceRepository
                .findByClientProfileProfileId(profileId)
                .orElse(new Preference());

        pref.setClientProfile(profile);
        pref.setDevelopmentApproach(WorkingStyle.valueOf(developmentApproach));
        pref.setExecutionStrategy(executionStrategy);
        pref.setReviewProcess(reviewProcess);

        return preferenceRepository.save(pref);
    }

    public List<ClientProfile> getAllProfiles() {
        return profileRepository.findAll();
    }

    public Optional<ClientProfile> getProfileById(Long id) {
        return profileRepository.findById(id);
    }

    public Optional<Preference> getPreferenceByProfileId(Long profileId) {
        return preferenceRepository.findByClientProfileProfileId(profileId);
    }

    public void deleteProfile(Long id) {
        profileRepository.deleteById(id);
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }
}
