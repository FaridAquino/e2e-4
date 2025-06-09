package org.e2e.labe2e04.review.infrastructure;

import org.e2e.labe2e04.review.domain.Review;
import org.e2e.labe2e04.review.dto.DriverReviewResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Set<Review> findByRating(Integer rating);

    Set<Review> findByAuthorId(Long authorId);

    Long countByAuthorId(Long authorId);

    Page<DriverReviewResponseDto> findByTargetId(Long targetId, Pageable pageable);
}