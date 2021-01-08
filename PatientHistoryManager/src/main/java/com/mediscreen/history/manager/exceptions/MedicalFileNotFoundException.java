package com.mediscreen.history.manager.exceptions;

/**
 * Exception that occurs when PatientManager does not accept user connection.
 *
 * @author Thierry Schreiner
 */
public class MedicalFileNotFoundException extends Exception {

    /**
     * Generated serial version UID.
     */
    private static final long serialVersionUID = -6930012383023567285L;

    /**
     * Class constructor.
     *
     * @param message
     */
    public MedicalFileNotFoundException(final String message) {
        super(message);
    }

    /**
     * No args empty class constructor.
     */
    public MedicalFileNotFoundException() {

    }
}
