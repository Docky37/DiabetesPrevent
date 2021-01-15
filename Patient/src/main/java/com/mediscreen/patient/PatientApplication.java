package com.mediscreen.patient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * This class contains the main function that launches the Spring Boot Application.
 *
 * @author Thierry Schreiner
 */
@SpringBootApplication
public class PatientApplication {

    /**
     * Method that launches the Spring Boot Application.
     *
     * @param args
     */
    public static void main(final String[] args) {
        SpringApplication.run(PatientApplication.class, args);
    }

    /**
     * Protected no args class constructor.
     */
    protected PatientApplication() {

    }

}
