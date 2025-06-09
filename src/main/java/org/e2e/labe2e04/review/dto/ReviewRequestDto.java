package org.e2e.labe2e04.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReviewRequestDto {
    @NotNull
    private String comment;

    @NotNull
    @Min(0)
    @Max(5)
    private Integer rating;

    @NotNull
    private Long rideId;

    @NotNull
    private Long targetId;
}