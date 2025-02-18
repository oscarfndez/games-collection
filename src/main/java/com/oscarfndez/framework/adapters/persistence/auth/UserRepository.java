package com.oscarfndez.framework.adapters.persistence.auth;

import com.oscarfndez.framework.core.model.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    // Since email is unique, we'll find users by email
    Optional<User> findByEmail(String email);
}
