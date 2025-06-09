package org.e2e.labe2e04.coordinate.infrastructure;

import org.e2e.labe2e04.coordinate.domain.Coordinate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CoordinateRepository extends JpaRepository<Coordinate, Long> {
    Optional<Coordinate> findByLatitudeAndLongitude(Double latitude, Double longitude);
}
