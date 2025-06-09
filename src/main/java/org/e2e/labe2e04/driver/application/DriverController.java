package org.e2e.labe2e04.driver.application;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.e2e.labe2e04.driver.domain.Driver;
import org.e2e.labe2e04.driver.domain.DriverService;
import org.e2e.labe2e04.driver.dto.DriverRequestDto;
import org.e2e.labe2e04.driver.dto.DriverResponseDto;
import org.e2e.labe2e04.driver.dto.UpdateDriverRequestDto;
import org.e2e.labe2e04.vehicle.dto.VehicleBasicDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/driver")
@RequiredArgsConstructor
public class DriverController {

    private final DriverService driverService;

    @GetMapping("/{id}")
    public ResponseEntity<DriverResponseDto> getDriverById(@PathVariable Long id) {
        DriverResponseDto driverResponseDto = driverService.getDriverById(id);
        return ResponseEntity.ok(driverResponseDto);
    }

    @GetMapping("/me")
    public ResponseEntity<DriverResponseDto> getCurrentDriver() {
        DriverResponseDto driverResponseDto = driverService.getCurrentDriver();
        return ResponseEntity.ok(driverResponseDto);
    }

    @PostMapping
    public ResponseEntity<Driver> createDriver(@Valid @RequestBody DriverRequestDto driverRequestDto) {
        Driver createdDriver = driverService.createDriver(driverRequestDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdDriver.getId())
                .toUri();
        return ResponseEntity.created(location).body(createdDriver);
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteDriverById() {
        driverService.deleteDriverById();
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/me")
    public ResponseEntity<DriverResponseDto> updateDriver(@Valid @RequestBody UpdateDriverRequestDto updateDriverRequestDto) {
        DriverResponseDto updatedDriver = driverService.updateDriver(updateDriverRequestDto);
        return ResponseEntity.ok(updatedDriver);
    }

    @PatchMapping("/me/location")
    public ResponseEntity<Driver> updateDriverLocation(@RequestParam Double latitude,
                                                       @RequestParam Double longitude) {
        Driver updatedDriver = driverService.updateDriverLocation(latitude, longitude);
        return ResponseEntity.ok(updatedDriver);
    }

    @PatchMapping("/me/car")
    public ResponseEntity<DriverResponseDto> updateDriverCar(@Valid @RequestBody VehicleBasicDto vehicleBasicDto) {
        DriverResponseDto driverResponseDto = driverService.updateDriverCar(vehicleBasicDto);
        return ResponseEntity.ok(driverResponseDto);
    }
}
