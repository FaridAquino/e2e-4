package org.e2e.labe2e04.passenger.domain;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.e2e.labe2e04.coordinate.domain.Coordinate;
import org.e2e.labe2e04.ride.domain.Ride;
import org.e2e.labe2e04.user.domain.User;
import org.e2e.labe2e04.userLocations.domain.UserLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Passenger extends User {
    @OneToMany(mappedBy = "passenger",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    private List<UserLocation> places = new ArrayList<>();

    @OneToMany(mappedBy = "passenger")
    private List<Ride> rides = new ArrayList<>();

    public List<Coordinate> getPlacesList() {
        List<Coordinate> coordinates = new ArrayList<>();

        for (UserLocation userLocation : this.places) {
            Coordinate newCoordinate = new Coordinate(userLocation.getCoordinate().getLatitude(), userLocation.getCoordinate().getLongitude());
            newCoordinate.setId(userLocation.getCoordinate().getId());
            coordinates.add(newCoordinate);
        }

        return coordinates;
    }

    public void addPlace(Coordinate coordinate, String description) {
        UserLocation userLocation = new UserLocation(this, coordinate, description);
        places.add(userLocation);
        coordinate.getPassengers().add(userLocation);
    }

    public void removePlace(Coordinate coordinate) {
        for (UserLocation userLocation : places) {
            if (userLocation.getPassenger().equals(this) && Objects.equals(userLocation.getCoordinate().getId(),
                    coordinate.getId())) {
                places.remove(userLocation);
                userLocation.getCoordinate().getPassengers().remove(userLocation);
                userLocation.setPassenger(null);
                userLocation.setCoordinate(null);
                break;
            }
        }
    }
}