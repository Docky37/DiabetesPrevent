package com.mediscreen.history;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * This class contains the main function that launches the Spring Boot Application.
 *
 * @author Thierry Schreiner
 */
@SpringBootApplication
public class PatientHistoryApplication {

    /**
     * Method that launches the Spring Boot Application.
     *
     * @param args
     */
    public static void main(final String[] args) {
        SpringApplication.run(PatientHistoryApplication.class, args);
    }

    /**
     * Protected no args class constructor.
     */
    protected PatientHistoryApplication() {

    }

}
