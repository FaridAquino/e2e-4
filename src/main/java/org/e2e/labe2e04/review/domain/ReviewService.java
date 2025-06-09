package org.e2e.labe2e04.review.domain;

import lombok.RequiredArgsConstructor;
import org.e2e.labe2e04.driver.exception.DriverNotFoundException;
import org.e2e.labe2e04.driver.infrastructure.DriverRepository;
import org.e2e.labe2e04.review.dto.DriverReviewResponseDto;
import org.e2e.labe2e04.review.dto.ReviewRequestDto;
import org.e2e.labe2e04.review.exception.ReviewNotFoundException;
import org.e2e.labe2e04.review.exception.ReviewTargetNotFoundException;
import org.e2e.labe2e04.review.infrastructure.ReviewRepository;
import org.e2e.labe2e04.ride.domain.Ride;
import org.e2e.labe2e04.ride.exception.RideNotFoundException;
import org.e2e.labe2e04.ride.infrastructure.RideRepository;
import org.e2e.labe2e04.security.utils.SecurityUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static org.e2e.labe2e04.security.utils.SecurityUtils.getCurrentUsername;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final RideRepository rideRepository;
    private final DriverRepository driverRepository;
    private final ModelMapper modelMapper;

    public Review createReview(ReviewRequestDto reviewRequestDto) {
        Ride ride = rideRepository
                .findById(reviewRequestDto.getRideId())
                .orElseThrow(RideNotFoundException::new);

        String currentUsername = SecurityUtils.getCurrentUsername();

        // Validar que el usuario autenticado sea el passenger o el driver del ride
        if (!(
                Objects.equals(currentUsername, ride.getPassenger().getUsername()) ||
                        Objects.equals(currentUsername, ride.getDriver().getUsername())
        )) {
            throw new AccessDeniedException("You are not authorized to leave a review for this ride.");
        }

        Review review = modelMapper.map(reviewRequestDto, Review.class);
        review.setRide(ride);

        // Setear autor y target
        if (Objects.equals(reviewRequestDto.getTargetId(), ride.getDriver().getId())) {
            review.setTarget(ride.getDriver());
            review.setAuthor(ride.getPassenger());
        } else if (Objects.equals(reviewRequestDto.getTargetId(), ride.getPassenger().getId())) {
            review.setTarget(ride.getPassenger());
            review.setAuthor(ride.getDriver());
        } else {
            throw new ReviewTargetNotFoundException();
        }

        return reviewRepository.save(review);
    }

    public void deleteReviewById(Long id) {
        Review review = reviewRepository
                .findById(id)
                .orElseThrow(ReviewNotFoundException::new);

        String currentUsername = SecurityUtils.getCurrentUsername();

        // Solo el autor de la review puede eliminarla
        if (!review.getAuthor().getEmail().equals(currentUsername)) {
            throw new AccessDeniedException("You do not have permission to delete this review.");
        }

        reviewRepository.deleteById(id);
    }

    public Page<DriverReviewResponseDto> getReviewsByDriverId(Long driverId, Pageable pageable) {
        if (!driverRepository.existsById(driverId)) {
            throw new DriverNotFoundException();
        }

        return reviewRepository.findByTargetId(driverId, pageable);
    }
}
