package org.e2e.labe2e04.review.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.e2e.labe2e04.ride.domain.Ride;
import org.e2e.labe2e04.user.domain.User;
import org.hibernate.annotations.Check;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Check(constraints = "rating BETWEEN 0 AND 5")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private Integer rating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User author;

    @OneToOne
    @JoinColumn(nullable = false, unique = true)
    private Ride ride;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User target;

    @Column(nullable = false)
    private String comment;
}