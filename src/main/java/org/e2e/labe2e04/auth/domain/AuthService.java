package org.e2e.labe2e04.auth.domain;

import lombok.RequiredArgsConstructor;
import org.e2e.labe2e04.auth.dto.AuthResponseDto;
import org.e2e.labe2e04.auth.dto.LoginRequestDto;
import org.e2e.labe2e04.auth.dto.RegisterRequestDto;
import org.e2e.labe2e04.driver.domain.Driver;
import org.e2e.labe2e04.driver.infrastructure.DriverRepository;
import org.e2e.labe2e04.passenger.domain.Passenger;
import org.e2e.labe2e04.passenger.infrastructure.PassengerRepository;
import org.e2e.labe2e04.security.jwt.JwtService;
import org.e2e.labe2e04.user.domain.User;
import org.e2e.labe2e04.user.domain.UserService;
import org.e2e.labe2e04.user.infrastructure.BaseUserRepository;
import org.e2e.labe2e04.user.domain.Role;
import org.e2e.labe2e04.vehicle.domain.Vehicle;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final PassengerRepository passengerRepository;
    private final BaseUserRepository<User> userRepository;
    private final DriverRepository driverRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final ModelMapper modelMapper;

    public AuthResponseDto login(LoginRequestDto loginRequestDto) {
        Optional<User> user;
        user = userRepository.findByEmail(loginRequestDto.getEmail());

        if (user.isEmpty()) throw new UsernameNotFoundException("Email is not registered");

        if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.get().getPassword()))
            throw new IllegalArgumentException("Password is incorrect");

        AuthResponseDto response = new AuthResponseDto();

        response.setToken(jwtService.generateToken(user.get()));
        return response;
    }

    public AuthResponseDto register(RegisterRequestDto registerRequestDto) {
        User newUser;

        if (registerRequestDto.getIsDriver()) {
            Driver driver = new Driver();
            driver.setCategory(registerRequestDto.getCategory());
            driver.setVehicle(modelMapper.map(registerRequestDto.getVehicle(), Vehicle.class));
            driver.setTrips(0);
            driver.setAvgRating(0.0);
            driver.setCreatedAt(ZonedDateTime.now());
            driver.setRole(Role.DRIVER);
            driver.setFirstName(registerRequestDto.getFirstName());
            driver.setLastName(registerRequestDto.getLastName());
            driver.setEmail(registerRequestDto.getEmail());
            driver.setPassword(passwordEncoder.encode(registerRequestDto.getPassword()));
            driver.setPhoneNumber(registerRequestDto.getPhoneNumber());

            // Map vehicle
//            Vehicle vehicle = new Vehicle();
//            vehicle.setBrand(registerRequestDto.getVehicle().getBrand());
//            vehicle.setModel(registerRequestDto.getVehicle().getModel());
//            vehicle.setLicensePlate(registerRequestDto.getVehicle().getLicensePlate());
//            vehicle.setFabricationYear(registerRequestDto.getVehicle().getFabricationYear());
//            vehicle.setCapacity(registerRequestDto.getVehicle().getCapacity());
//            vehicle.setColor(registerRequestDto.getVehicle().getColor());
//            driver.setVehicle(vehicle);

            newUser = driverRepository.save(driver);
        } else {
            // Crear Passenger
            Passenger passenger = new Passenger();
            passenger.setCreatedAt(ZonedDateTime.now());
            passenger.setRole(Role.PASSENGER);
            passenger.setFirstName(registerRequestDto.getFirstName());
            passenger.setLastName(registerRequestDto.getLastName());
            passenger.setEmail(registerRequestDto.getEmail());
            passenger.setPassword(passwordEncoder.encode(registerRequestDto.getPassword()));
            passenger.setPhoneNumber(registerRequestDto.getPhoneNumber());
            passenger.setAvgRating(0.0);
            passenger.setTrips(0);

            newUser = passengerRepository.save(passenger);
        }

        String token = jwtService.generateToken(newUser);

        return new AuthResponseDto(token);
    }
}
