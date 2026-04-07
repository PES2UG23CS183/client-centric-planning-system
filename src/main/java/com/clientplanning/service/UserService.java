package com.clientplanning.service;

import com.clientplanning.model.*;
import com.clientplanning.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * DELISHA (PES2UG23CS166)
 *
 * GRASP Principle Applied: Information Expert
 *   UserService is the information expert for all user-related data.
 *   Since it has direct access to UserRepository, ClientRepository,
 *   and BusinessAnalystRepository, it is best placed to handle all
 *   logic involving users, clients, and analysts. Assigning this
 *   responsibility here avoids spreading user logic across multiple classes.
 *
 * Design Pattern Applied: Singleton
 *   Spring's @Service annotation ensures that only one instance of
 *   UserService is created and shared across the entire application
 *   context. This is the Singleton pattern — a single shared instance
 *   manages all user operations without creating redundant objects.
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private BusinessAnalystRepository analystRepository;

    // Register a new user and create their role-specific record
    public User register(String name, String email, String password, String role,
                         String organisation, String contactInfo,
                         String specialisation, int experience) {

        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already registered.");
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(role);
        User saved = userRepository.save(user);

        if ("CLIENT".equals(role)) {
            Client client = new Client();
            client.setOrganisation(organisation != null ? organisation : "");
            client.setContactInfo(contactInfo != null ? contactInfo : "");
            client.setUser(saved);
            clientRepository.save(client);
        } else if ("BUSINESS_ANALYST".equals(role)) {
            BusinessAnalyst analyst = new BusinessAnalyst();
            analyst.setSpecialisation(specialisation != null ? specialisation : "");
            analyst.setExperience(experience);
            analyst.setUser(saved);
            analystRepository.save(analyst);
        }

        return saved;
    }

    // Validate login credentials
    public Optional<User> login(String email, String password) {
        return userRepository.findByEmail(email)
                .filter(u -> u.getPassword().equals(password));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public List<BusinessAnalyst> getAllAnalysts() {
        return analystRepository.findAll();
    }
}
