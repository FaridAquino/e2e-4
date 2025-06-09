package org.e2e.labe2e04.passenger.infrastructure;

import jakarta.transaction.Transactional;
import org.e2e.labe2e04.passenger.domain.Passenger;
import org.e2e.labe2e04.user.infrastructure.BaseUserRepository;

import java.util.Optional;

@Transactional
public interface PassengerRepository extends BaseUserRepository<Passenger> {
    Optional<Passenger> findByEmail(String username);
}
