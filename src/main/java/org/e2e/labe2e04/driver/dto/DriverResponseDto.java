package org.e2e.labe2e04.driver.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.e2e.labe2e04.driver.domain.Category;
import org.e2e.labe2e04.vehicle.dto.VehicleBasicDto;

@Getter
@Setter
@NoArgsConstructor
public class DriverResponseDto {
    private Long id;

    @NotNull
    private Category category;

    @NotNull
    @Size(min = 2, max = 50)
    private String firstName;

    @NotNull
    @Size(min = 2, max = 50)
    private String lastName;

    @NotNull
    @Min(value = 0)
    private Integer trips;

    @NotNull
    @Min(value = 0)
    private Double avgRating;

    @Valid
    @NotNull
    private VehicleBasicDto vehicle;
}