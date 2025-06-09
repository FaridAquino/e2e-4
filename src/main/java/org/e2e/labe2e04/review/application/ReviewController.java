package org.e2e.labe2e04.review.application;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.e2e.labe2e04.review.domain.Review;
import org.e2e.labe2e04.review.domain.ReviewService;
import org.e2e.labe2e04.review.dto.DriverReviewResponseDto;
import org.e2e.labe2e04.review.dto.ReviewRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<Review> createReview(@Valid @RequestBody ReviewRequestDto reviewRequestDto) {
        Review createdReview = reviewService.createReview(reviewRequestDto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdReview.getId())
                .toUri();
        return ResponseEntity.created(location).body(createdReview);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Review> deleteReviewById(@PathVariable Long id) {
        reviewService.deleteReviewById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{driverId}")
    public ResponseEntity<Page<DriverReviewResponseDto>> getDriverReviewsByDriverId(@PathVariable Long driverId,
                                                                                    @RequestParam(defaultValue = "0") Integer page,
                                                                                    @RequestParam(defaultValue = "10") Integer size) {
        Page<DriverReviewResponseDto> driverReviewResponseDto = reviewService.getReviewsByDriverId(driverId,
                PageRequest.of(page, size));
        return ResponseEntity.ok(driverReviewResponseDto);
    }
}