package com.mediscreen.history.manager.exceptions;

/**
 * Exception that occurs when we try to add a new medical file to a patient who already has one.
 *
 * @author Thierry Schreiner
 */
public class ConflictException extends Exception {

    /**
     * Generated serial version UID.
     */
    private static final long serialVersionUID = -8308868808151070370L;

    /**
     * Class constructor.
     *
     * @param message
     */
    public ConflictException(final String message) {
        super(message);
    }

    /**
     * No args empty class constructor.
     */
    public ConflictException() {

    }

}
