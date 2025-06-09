package org.e2e.labe2e04.ride.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.e2e.labe2e04.coordinate.domain.Coordinate;
import org.e2e.labe2e04.driver.domain.Driver;
import org.e2e.labe2e04.passenger.domain.Passenger;

import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Ride {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Status status;

    private ZonedDateTime arrivalDate;

    private ZonedDateTime departureDate;

    @JoinColumn(nullable = false)
    @OneToOne(cascade = CascadeType.ALL)
    private Coordinate destinationCoordinates;

    @JoinColumn(nullable = false)
    @OneToOne(cascade = CascadeType.ALL)
    private Coordinate originCoordinates;

    @ManyToOne
    @JoinColumn
    private Driver driver;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Passenger passenger;

    @Column(nullable = false)
    private String destinationName;

    @JoinColumn(nullable = false)
    private String originName;
}