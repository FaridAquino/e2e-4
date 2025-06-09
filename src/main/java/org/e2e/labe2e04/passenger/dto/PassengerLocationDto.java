package org.e2e.labe2e04.passenger.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.e2e.labe2e04.coordinate.dto.CoordinateDto;

@Getter
@Setter
@NoArgsConstructor
public class PassengerLocationDto {
    @Valid
    @NotNull
    private CoordinateDto coordinate;

    @NotNull
    @Size(min = 2, max = 255)
    private String description;
}
