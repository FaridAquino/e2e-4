package org.e2e.labe2e04.vehicle.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VehicleBasicDto {
    @NotNull
    @Size(min = 2, max = 50)
    private String brand;

    @NotNull
    @Size(min = 2, max = 50)
    private String model;

    @NotNull
    @Size(min = 2, max = 50)
    private String licensePlate;

    @NotNull
    @Min(1900)
    @Max(2025)
    private Integer fabricationYear;

    private Integer capacity;

    @NotNull
    @Size(min = 2, max = 50)
    private String color;
}