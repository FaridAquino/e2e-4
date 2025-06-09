package org.e2e.labe2e04.ride.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
public class RideResponseDto {
    @NotNull
    private Long id;

    @NotNull
    @Size(min = 2, max = 255)
    private String originName;

    @NotNull
    @Size(min = 2, max = 255)
    private String destinationName;

    @NotNull
    @Min(0)
    private Double price;

    @NotNull
    private ZonedDateTime departureDate;
}