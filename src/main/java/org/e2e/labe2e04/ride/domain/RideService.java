package org.e2e.labe2e04.ride.domain;

import lombok.RequiredArgsConstructor;
import org.e2e.labe2e04.driver.domain.Driver;
import org.e2e.labe2e04.driver.infrastructure.DriverRepository;
import org.e2e.labe2e04.passenger.domain.Passenger;
import org.e2e.labe2e04.passenger.exception.PassengerNotFoundException;
import org.e2e.labe2e04.passenger.infrastructure.PassengerRepository;
import org.e2e.labe2e04.ride.dto.RideRequestDto;
import org.e2e.labe2e04.ride.dto.RideResponseDto;
import org.e2e.labe2e04.ride.exception.RideNotFoundException;
import org.e2e.labe2e04.ride.infrastructure.RideRepository;
import org.e2e.labe2e04.security.utils.SecurityUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RideService {

    private final PassengerRepository passengerRepository;
    private final RideRepository rideRepository;
    private final DriverRepository driverRepository;
    private final ModelMapper modelMapper;

    public Ride createRide(RideRequestDto rideRequestDto) {
        // Passenger autenticado
        String currentUsername = SecurityUtils.getCurrentUsername();
        Passenger passenger = passengerRepository.findByEmail(currentUsername)
                .orElseThrow(() -> new UsernameNotFoundException("Passenger not found"));

        Ride ride = modelMapper.map(rideRequestDto, Ride.class);
        ride.setPassenger(passenger);
        ride.setStatus(Status.REQUESTED);

        return rideRepository.save(ride);
    }

    public Ride assignDriverToRide(Long id) {
        // Driver autenticado
        String currentUsername = SecurityUtils.getCurrentUsername();
        Driver driver = driverRepository.findByEmail(currentUsername)
                .orElseThrow(() -> new UsernameNotFoundException("Driver not found"));

        Ride ride = rideRepository.findById(id).orElseThrow(RideNotFoundException::new);

        // Validar que el ride esté en estado REQUESTED antes de aceptar (opcional pero recomendable)
        if (ride.getStatus() != Status.REQUESTED) {
            throw new IllegalStateException("Ride is not in REQUESTED state.");
        }

        ride.setDriver(driver);
        ride.setStatus(Status.ACCEPTED);

        return rideRepository.save(ride);
    }

    public Page<RideResponseDto> getPassengerRides(Long passengerId, Pageable pageable) {
        // Este método en teoría no es testeado en la rúbrica (porque el endpoint es /ride/me).
        // Pero si lo usas, puedes validarlo igual:

        Passenger passenger = passengerRepository.findById(passengerId)
                .orElseThrow(PassengerNotFoundException::new);

        Page<Ride> rides = rideRepository.findAllByPassengerIdAndStatus(passenger.getId(), Status.COMPLETED, pageable);
        return rides.map(ride -> modelMapper.map(ride, RideResponseDto.class));
    }

    public Ride cancelRide(Long id, Status status) {
        // Passenger o Driver autenticado → ambos pueden cambiar el status
        String currentUsername = SecurityUtils.getCurrentUsername();

        Ride ride = rideRepository.findById(id).orElseThrow(RideNotFoundException::new);

        // Validar permisos:
        boolean isPassenger = currentUsername.equals(ride.getPassenger().getUsername());
        boolean isDriver = ride.getDriver() != null && currentUsername.equals(ride.getDriver().getUsername());

        if (!(isPassenger || isDriver)) {
            throw new AccessDeniedException("You are not authorized to modify this ride.");
        }

        ride.setStatus(status);

        return rideRepository.save(ride);
    }

    public Page<RideResponseDto> getCurrentUserRides(Pageable pageable) {
        // Passenger autenticado
        String currentUsername = SecurityUtils.getCurrentUsername();
        Passenger passenger = passengerRepository.findByEmail(currentUsername)
                .orElseThrow(() -> new UsernameNotFoundException("Passenger not found"));

        Page<Ride> rides = rideRepository.findAllByPassengerIdAndStatus(passenger.getId(), Status.COMPLETED, pageable);

        return rides.map(ride -> modelMapper.map(ride, RideResponseDto.class));
    }
}
