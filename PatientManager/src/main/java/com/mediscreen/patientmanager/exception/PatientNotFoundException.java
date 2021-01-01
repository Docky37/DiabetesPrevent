package com.mediscreen.patientmanager.exception;

/**
 * Exception that occurs when PatientManager does not accept user connection.
 *
 * @author Thierry Schreiner
 */
public class PatientNotFoundException extends Exception {

    /**
     * Generated serial version UID.
     */
    private static final long serialVersionUID = -6930012383023567285L;

    /**
     * No args empty class constructor.
     *
     * @param message
     */
    public PatientNotFoundException(final String message) {
        super(message);
    }

    /**
     * No args empty class constructor.
     */
    public PatientNotFoundException() {

    }
}
