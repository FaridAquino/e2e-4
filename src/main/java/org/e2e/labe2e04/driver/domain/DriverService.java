package org.e2e.labe2e04.driver.domain;

import lombok.RequiredArgsConstructor;
import org.e2e.labe2e04.coordinate.domain.Coordinate;
import org.e2e.labe2e04.coordinate.infrastructure.CoordinateRepository;
import org.e2e.labe2e04.driver.dto.DriverRequestDto;
import org.e2e.labe2e04.driver.dto.DriverResponseDto;
import org.e2e.labe2e04.driver.dto.UpdateDriverRequestDto;
import org.e2e.labe2e04.driver.exception.DriverNotFoundException;
import org.e2e.labe2e04.driver.infrastructure.DriverRepository;
import org.e2e.labe2e04.exception.ConflictException;
import org.e2e.labe2e04.security.utils.SecurityUtils;
import org.e2e.labe2e04.vehicle.dto.VehicleBasicDto;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DriverService {

    private final DriverRepository driverRepository;
    private final CoordinateRepository coordinateRepository;
    private final ModelMapper modelMapper;

    public DriverResponseDto getDriverById(Long id) {
        Driver driver = driverRepository
                .findById(id)
                .orElseThrow(DriverNotFoundException::new);
        return modelMapper.map(driver, DriverResponseDto.class);
    }

    public DriverResponseDto getCurrentDriver() {
        String currentUsername = SecurityUtils.getCurrentUsername();
        Driver driver = driverRepository.findByEmail(currentUsername)
                .orElseThrow(() -> new UsernameNotFoundException("Driver not found"));
        return modelMapper.map(driver, DriverResponseDto.class);
    }

    public Driver createDriver(DriverRequestDto driverRequestDto) {
        if (driverRepository.existsByEmail(driverRequestDto.getEmail()))
            throw new ConflictException("Driver with this email already exists");
        return driverRepository.save(modelMapper.map(driverRequestDto, Driver.class));
    }

    public void deleteDriverById() {
        String currentUsername = SecurityUtils.getCurrentUsername();
        Driver driver = driverRepository.findByEmail(currentUsername)
                .orElseThrow(() -> new UsernameNotFoundException("Driver not found"));
        driverRepository.delete(driver);
    }

    public DriverResponseDto updateDriver(UpdateDriverRequestDto driverDto) {
        String currentUsername = SecurityUtils.getCurrentUsername();
        Driver driver = driverRepository.findByEmail(currentUsername)
                .orElseThrow(() -> new UsernameNotFoundException("Driver not found"));

        modelMapper.map(driverDto, driver);
        return modelMapper.map(driverRepository.save(driver), DriverResponseDto.class);
    }

    public Driver updateDriverLocation(Double latitude, Double longitude) {
        String currentUsername = SecurityUtils.getCurrentUsername();
        Driver driver = driverRepository.findByEmail(currentUsername)
                .orElseThrow(() -> new UsernameNotFoundException("Driver not found"));

        Coordinate coordinate = new Coordinate();
        coordinate.setLatitude(latitude);
        coordinate.setLongitude(longitude);
        coordinateRepository.save(coordinate);

        driver.setCoordinate(coordinate);
        return driverRepository.save(driver);
    }

    public DriverResponseDto updateDriverCar(VehicleBasicDto vehicleBasicDto) {
        String currentUsername = SecurityUtils.getCurrentUsername();
        Driver driver = driverRepository.findByEmail(currentUsername)
                .orElseThrow(() -> new UsernameNotFoundException("Driver not found"));

        modelMapper.map(vehicleBasicDto, driver.getVehicle());
        return modelMapper.map(driverRepository.save(driver), DriverResponseDto.class);
    }
}
