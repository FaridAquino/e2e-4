package org.e2e.labe2e04.ride.application;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.e2e.labe2e04.ride.domain.Ride;
import org.e2e.labe2e04.ride.domain.RideService;
import org.e2e.labe2e04.ride.domain.Status;
import org.e2e.labe2e04.ride.dto.RideRequestDto;
import org.e2e.labe2e04.ride.dto.RideResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/ride")
@RequiredArgsConstructor
public class RideController {

    private final RideService rideService;

    @PostMapping
    public ResponseEntity<Ride> createRide(@Valid @RequestBody RideRequestDto rideRequestDto) {
        Ride createdRide = rideService.createRide(rideRequestDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdRide.getId())
                .toUri();
        return ResponseEntity.created(location).body(createdRide);
    }

    @PatchMapping("/assign/{rideId}")
    public ResponseEntity<Ride> assignDriverToRide(@PathVariable Long rideId) {
        Ride updatedRide = rideService.assignDriverToRide(rideId);
        return ResponseEntity.ok(updatedRide);
    }

    @PatchMapping("/{id}/status/{status}")
    public ResponseEntity<Ride> cancelRide(@PathVariable Long id, @PathVariable Status status) {
        Ride ride = rideService.cancelRide(id, status);
        return ResponseEntity.ok(ride);
    }

    @GetMapping("/me")
    public ResponseEntity<Page<RideResponseDto>> getCurrentUserRides(@RequestParam(defaultValue = "0") Integer page,
                                                                     @RequestParam(defaultValue = "10") Integer size) {
        Page<RideResponseDto> rides = rideService.getCurrentUserRides(PageRequest.of(page, size));
        return ResponseEntity.ok(rides);
    }
}
