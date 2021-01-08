package com.mediscreen.history.manager.exceptions;

/**
 * Exception that occurs when PatientManager does not accept user connection.
 *
 * @author Thierry Schreiner
 */
public class UnauthorizedException extends Exception {

    /**
     * Generated serial version UID.
     */
    private static final long serialVersionUID = -3977981212926366709L;

    /**
     * Class constructor.
     *
     * @param message
     */
    public UnauthorizedException(final String message) {
        super(message);
    }

    /**
     * No args empty class constructor.
     */
    public UnauthorizedException() {

    }

}