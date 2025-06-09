package org.e2e.labe2e04.passenger.exception;

import org.e2e.labe2e04.exception.ResourceNotFoundException;

public class PassengerNotFoundException extends ResourceNotFoundException {
    public PassengerNotFoundException() {
        super("Passenger not found");
    }
}
