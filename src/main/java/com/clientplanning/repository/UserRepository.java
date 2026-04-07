package com.clientplanning.repository;

import com.clientplanning.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// DELISHA (PES2UG23CS166)
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
