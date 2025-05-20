package org.sspd.myatdental.ErrorHandler;

// Custom exception for appointment conflicts
public class AppointmentConflictException extends Exception {
    public AppointmentConflictException(String message) {
        super(message);
    }
}
