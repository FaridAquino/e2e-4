package org.e2e.labe2e04.user.infrastructure;

import org.e2e.labe2e04.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BaseUserRepository<T extends User> extends JpaRepository<T, Long> {
    boolean existsByEmail(String email);

    Optional<T> findByEmail(String email);
}