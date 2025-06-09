package org.e2e.labe2e04.userLocations.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PassengerCoordinateId implements Serializable {
    @Column
    private Long passengerId;

    @Column
    private Long coordinateId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        PassengerCoordinateId that = (PassengerCoordinateId) o;

        return Objects.equals(passengerId, that.passengerId) &&
                Objects.equals(coordinateId, that.coordinateId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(passengerId, coordinateId);
    }
}