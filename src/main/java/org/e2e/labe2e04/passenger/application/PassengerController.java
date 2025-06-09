package org.e2e.labe2e04.passenger.application;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.e2e.labe2e04.passenger.domain.Passenger;
import org.e2e.labe2e04.passenger.domain.PassengerService;
import org.e2e.labe2e04.passenger.dto.PassengerLocationDto;
import org.e2e.labe2e04.passenger.dto.PassengerRequestDto;
import org.e2e.labe2e04.passenger.dto.PassengerResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/passenger")
@RequiredArgsConstructor
public class PassengerController {

    private final PassengerService passengerService;

    @GetMapping("/{id}")
    public ResponseEntity<PassengerResponseDto> getPassengerById(@PathVariable Long id) {
        return ResponseEntity.ok(passengerService.getPassengerById(id));
    }

    @GetMapping("/me")
    public ResponseEntity<PassengerResponseDto> getCurrentPassenger() {
        return ResponseEntity.ok(passengerService.getCurrentPassenger());
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deletePassengerById() {
        passengerService.deletePassengerById();
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/me")
    public ResponseEntity<PassengerResponseDto> updatePassenger(@Valid @RequestBody PassengerRequestDto passengerRequestDto) {
        // OJO: en tu Service no tienes updatePassenger — te recomiendo agregarlo similar a DriverService.
        // Por ahora te dejo el endpoint preparado:
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @PostMapping("/me/places")
    public ResponseEntity<Passenger> addPassengerPlace(@Valid @RequestBody PassengerLocationDto passengerLocationDto) {
        Passenger updatedPassenger = passengerService.addPassengerPlace(passengerLocationDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .buildAndExpand()
                .toUri();
        return ResponseEntity.created(location).body(updatedPassenger);
    }

    @GetMapping("/me/places")
    public ResponseEntity<List<PassengerLocationDto>> getPassengerPlaces() {
        List<PassengerLocationDto> places = passengerService.getPassengerPlaces();
        return ResponseEntity.ok(places);
    }

    @DeleteMapping("/me/places/{coordinateId}")
    public ResponseEntity<Void> deletePassengerPlace(@PathVariable Long coordinateId) {
        passengerService.deletePassengerPlace(coordinateId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<Passenger> createPassenger(@Valid @RequestBody PassengerRequestDto passengerRequestDto) {
        Passenger passenger = passengerService.createPassenger(passengerRequestDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(passenger.getId())
                .toUri();
        return ResponseEntity.created(location).body(passenger);
    }
}
