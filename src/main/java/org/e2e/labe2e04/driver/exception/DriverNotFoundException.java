package org.e2e.labe2e04.driver.exception;

import org.e2e.labe2e04.exception.ResourceNotFoundException;

public class DriverNotFoundException extends ResourceNotFoundException {
    public DriverNotFoundException() {
        super("Driver not found");
    }
}
