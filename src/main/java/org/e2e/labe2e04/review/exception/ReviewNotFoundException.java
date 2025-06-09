package org.e2e.labe2e04.review.exception;

import org.e2e.labe2e04.exception.ResourceNotFoundException;

public class ReviewNotFoundException extends ResourceNotFoundException {
    public ReviewNotFoundException() {
        super("Review not found");
    }
}
