package org.e2e.labe2e04.driver.infrastructure;


import org.e2e.labe2e04.driver.domain.Category;
import org.e2e.labe2e04.driver.domain.Driver;
import org.e2e.labe2e04.user.infrastructure.BaseUserRepository;

import java.util.List;
import java.util.Optional;

public interface DriverRepository extends BaseUserRepository<Driver> {
    List<Driver> findAllByCategory(Category category);

    Optional<Driver> findByEmail(String email);
}
