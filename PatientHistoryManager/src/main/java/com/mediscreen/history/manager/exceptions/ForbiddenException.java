package com.mediscreen.history.manager.exceptions;

/**
 * Exception that occurs when authorized user try to use a resource that is forbidden for him.
 *
 * @author Thierry Schreiner
 */
public class ForbiddenException extends Exception {

    /**
     * Generated serial version UID.
     */
    private static final long serialVersionUID = -4342038739901965122L;

    /**
     * No args empty class constructor.
     */
    public ForbiddenException() {

    }

    /**
     * Class constructor.
     *
     * @param message
     */
    public ForbiddenException(final String message) {
        super(message);
    }

}
