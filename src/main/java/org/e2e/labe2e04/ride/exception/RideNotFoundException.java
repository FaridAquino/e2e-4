package org.e2e.labe2e04.ride.exception;

import org.e2e.labe2e04.exception.ResourceNotFoundException;

public class RideNotFoundException extends ResourceNotFoundException {
    public RideNotFoundException() {
        super("Ride not found");
    }
}
