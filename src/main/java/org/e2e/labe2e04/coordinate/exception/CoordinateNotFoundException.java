package org.e2e.labe2e04.coordinate.exception;

public class CoordinateNotFoundException extends RuntimeException {
    public CoordinateNotFoundException() {
        super("Coordinate not found");
    }
}
